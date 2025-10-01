package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.commons.Coordenada;
import ar.utn.ba.ddsi.commons.DivisorEnLotes;
import ar.utn.ba.ddsi.models.dtos.apigob.GeorefRequestMultipleDTO;
import ar.utn.ba.ddsi.models.dtos.apigob.GeorefRequestDTO;
import ar.utn.ba.ddsi.models.dtos.apigob.GeorreferenciacionDTO;
import ar.utn.ba.ddsi.models.dtos.apigob.ResultadoGeoDTO;
import ar.utn.ba.ddsi.models.dtos.output.HechoOutputDTO;
import ar.utn.ba.ddsi.models.dtos.external.HechoFuenteDTO;
import ar.utn.ba.ddsi.models.entities.*;
import ar.utn.ba.ddsi.models.entities.ubicacion.Departamento;
import ar.utn.ba.ddsi.models.repositories.ICategoriaRepository;
import ar.utn.ba.ddsi.models.repositories.IFuentesRepository;
import ar.utn.ba.ddsi.models.repositories.IHechosRepository;
import ar.utn.ba.ddsi.models.repositories.IDepartamentosRepository;
import ar.utn.ba.ddsi.services.IHechosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HechosService implements IHechosService {
    private final ApplicationEventPublisher applicationEventPublisher;
    private IHechosRepository hechosRepository;

    private final Map<OrigenHecho, WebClient> webClients = new HashMap<OrigenHecho, WebClient>();
    private final WebClient webClientGeoref;
    private LocalDateTime fechaUltimaActualizacion = LocalDate.parse("01/01/1000", DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay();

    @Autowired
    private ICategoriaRepository categoriaRepository;
    @Autowired
    private IDepartamentosRepository departamentosRepository;
    @Autowired
    private IFuentesRepository fuentesRepository;

    @Autowired
    public HechosService(IHechosRepository hechosRepository, @Value("${fuente.estatica.api.base-url}") String fuenteEstaticaApiBaseUrl, @Value("${fuente.dinamica.api.base-url}") String fuenteDinamicaApiBaseUrl, @Value("${fuente.proxy.api.base-url}") String fuenteProxyApiBaseUrl, @Value("${georef.api.base-url}") String georefApiBaseUrl, ApplicationEventPublisher applicationEventPublisher) {
        this.hechosRepository = hechosRepository;
        this.webClients.put(OrigenHecho.DATASET, WebClient.builder().baseUrl(fuenteEstaticaApiBaseUrl).build());
        this.webClients.put(OrigenHecho.CONTRIBUYENTE, WebClient.builder().baseUrl(fuenteDinamicaApiBaseUrl).build());
        this.webClients.put(OrigenHecho.PROXY, WebClient.builder().baseUrl(fuenteProxyApiBaseUrl).build());
        this.webClientGeoref = WebClient.builder().baseUrl(georefApiBaseUrl).build();
        this.applicationEventPublisher = applicationEventPublisher;
    }

    // --- Métodos expuestos al controller -------------------------------------------------------------------------------

    @Override
    public List<HechoOutputDTO> buscarTodos(Map<String, String> params) {
        Criterio filtrosDeUsuario = new Criterio(params);

        List<Hecho> hechosLocales = hechosRepository.findAll();

        List<Hecho> actualizadosConMetamapa = actualizarListaConHechosMetamapa(hechosLocales);

        return filtrosDeUsuario.aplicarA(actualizadosConMetamapa)
            .stream().map(HechoOutputDTO::fromEntity).toList();
    }

    // -------------------------------------------------------- Métodos de trabajo interno ---------------------------------------------------------------------------//
    @Override
    public Hecho obtenerPorId(Long id){
        return hechosRepository.findById(id).orElse(null);
    }

    @Override
    public void actualizarHechos(){
        Mono<List<HechoFuenteDTO>> monoEstatica = crearMonoPeticionHechos(webClients.get(OrigenHecho.DATASET));
        Mono<List<HechoFuenteDTO>> monoDinamica = crearMonoPeticionHechos(webClients.get(OrigenHecho.CONTRIBUYENTE));
        Mono<List<HechoFuenteDTO>> monoProxy = crearMonoPeticionHechos(webClients.get(OrigenHecho.PROXY));

        // Ejecuta los mono y junta los resultados
        List<HechoFuenteDTO> hechosFuente = new ArrayList<>();
        List.of(monoEstatica, monoDinamica, monoProxy).forEach(mono -> {
            List<HechoFuenteDTO> hechosFuenteDTO = mono.block();
            if (hechosFuenteDTO != null) {
                hechosFuente.addAll(hechosFuenteDTO);
            }
        });

        // Carga las fuentes del repo y los setea en los hechos obtenidos
        List<Fuente> fuentes = fuentesRepository.findAll();
        List<Hecho> hechos = new ArrayList<>(hechosFuente.stream().map(dto -> {
            Hecho hecho = dto.toEntity();
            Fuente fuente = fuentes.stream().filter(f ->
                f.getTipoDeFuente().name().equals(dto.getTipoDeFuente())
                    && f.getSubfuenteId().equals(dto.getSubfuenteId())
            ).findFirst().orElse(null);

            hecho.getIdExterno().setFuente(fuente);
            return hecho;
        }).toList());

        // Busca los hechos que comparten ID Externo con los actualizados, pues van a tener que modificarlos
        List<IdExterno> ids = hechos.stream().map(Hecho::getIdExterno).toList();
        List<Hecho> hechosAModificar = hechosRepository.findAllByIdExternoIn(ids);

        // Aplica normalizaciones
        normalizarCategoria(hechos);
        normalizarUbicacion(hechos);

        // Para cada hecho normalizado, si ya existía en el agregador, lo actualiza
        for (int i = 0; i < hechos.size(); i++) {
            Hecho hechoNormalizado = hechos.get(i);
            Hecho viejo = hechosAModificar.stream()
                .filter(h -> h.getIdExterno().equals(hechoNormalizado.getIdExterno()))
                .findFirst().orElse(null);
            if (viejo != null) {
                modificarHecho(viejo, hechoNormalizado);
                hechos.set (i, viejo);
            } else{
                hechoNormalizado.setFechaUltimaActualizacion(LocalDateTime.now());
            }
        }

        hechosRepository.saveAll(hechos);
        fechaUltimaActualizacion = LocalDateTime.now();
        applicationEventPublisher.publishEvent(new HechosModificadosEvent(hechos));
    }


    @Override
    public void normalizarCategoria(List<Hecho> hechos){
        List<Categoria> listaDeCategorias = categoriaRepository.findAll();

        hechos.forEach(hecho -> {
            String categoriaOriginal = hecho.getCategoria().getNombre();

            for (Categoria categoria : listaDeCategorias) {
                if (categoria.esLaMisma(categoriaOriginal)) {
                    hecho.setCategoria(categoria);
                    return;
                }
            }
            hecho.setCategoria(null);
        });
    }


    @Override
    public void normalizarUbicacion(List<Hecho> hechos){
        List<Hecho> requierenGeorref = new ArrayList<>();
        hechos.forEach(hecho -> {
            if (hecho.getLugarAcontecimiento() != null) {
                requierenGeorref.add(hecho);
            }
            else {
                // TODO Normalización tipo categoría
            }
        });

        Map<Coordenada, List<Hecho>> hechosPorCoordenada = requierenGeorref.stream()
            .collect(Collectors.groupingBy(Hecho::getLugarAcontecimiento));
        List<Coordenada> coordenadas = hechosPorCoordenada.keySet().stream().toList();

        List<List<Coordenada>> lotes = DivisorEnLotes.dividir(coordenadas, 300);

        List<Map<Coordenada, Departamento>> listaDeMapas = Flux.fromIterable(lotes)
            .concatMap(this::georreferenciacionInversa)
            .collectList()
            .block();

        if (listaDeMapas == null) {
            return;
        }

        Map<Coordenada, Departamento> combinados = new HashMap<>();
        listaDeMapas.forEach(combinados::putAll);

        combinados.forEach((coordenada, departamento) ->
            hechosPorCoordenada.get(coordenada).forEach(hechoAModificar ->
                hechoAModificar.setDepartamento(departamento)
            )
        );

    }


    public Mono<Map<Coordenada, Departamento>> georreferenciacionInversa(List<Coordenada> coordenadas){

        List<GeorefRequestDTO> coordFormat = coordenadas.stream().map(GeorefRequestDTO::fromCoordenada).toList();
        List<Departamento> departamentos = departamentosRepository.findAll();

        GeorefRequestMultipleDTO reqBody = new GeorefRequestMultipleDTO();
        reqBody.setUbicaciones(coordFormat);

        return webClientGeoref
            .post()
            .uri("/ubicacion")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(reqBody)
            .retrieve()
            .onStatus(
                HttpStatusCode::isError,
                clientResponse -> clientResponse.bodyToMono(String.class)
                    .map(body -> new RuntimeException("Error en API Georef: " + body))
            )
            .bodyToMono(GeorreferenciacionDTO.class)
            .map(dto -> Optional.ofNullable(dto.getResultados()).orElse(Collections.emptyList()))
            .map(listaRes -> listaRes.stream().map(ResultadoGeoDTO::getUbicacion).toList())
            .map(ubicaciones -> {
                Map<Coordenada, Departamento> departamentosPorCoordenada = new HashMap<>();
                ubicaciones.forEach(ubicacion -> {
                    Departamento departamento = departamentos.stream().filter(m ->
                            m.getNombre().equals(ubicacion.getDepartamento_nombre()) &&
                                m.getProvincia().getNombre().equals(ubicacion.getProvincia_nombre()))
                        .findFirst().orElse(null);
                    Coordenada coord = new Coordenada(ubicacion.getLat(), ubicacion.getLon());
                    departamentosPorCoordenada.put(coord, departamento);
                });
                return departamentosPorCoordenada;
            });
    }


    @Override
    public List<Hecho> getFromMetaMapa() {
        return webClients.get(OrigenHecho.PROXY)
            .get()
            .uri(uriBuilder -> uriBuilder
                .path("/api/hechos/metamapaInstance")     // TODO: Ver si nos conviene pedir una fuente MetaMapa específica
                .build()
            )
            .retrieve()
            .bodyToFlux(HechoFuenteDTO.class)
            .map(HechoFuenteDTO::toEntity)
            .collectList().block();
    }


    @Override
    public List<Hecho> actualizarListaConHechosMetamapa(List<Hecho> hechosLocales) {
        List<Hecho> hechosMetaMapa = this.getFromMetaMapa();

        Map<IdExterno, Hecho> hechosPorId = hechosLocales.stream()
            .collect(Collectors.toMap(Hecho::getIdExterno, h -> h));

        for (Hecho hechoMetamapa : hechosMetaMapa) {
            if (hechosPorId.containsKey(hechoMetamapa.getIdExterno())) {
                hechoMetamapa.setId(hechosPorId.get(hechoMetamapa.getIdExterno()).getId());
                hechosPorId.put(hechoMetamapa.getIdExterno(), hechoMetamapa);
            }
        }

        return new ArrayList<>(hechosPorId.values());
    }


    private Mono<List<HechoFuenteDTO>> crearMonoPeticionHechos(WebClient webClient) {
        String fechaUltimaActualizacionStr = fechaUltimaActualizacion.format(DateTimeFormatter.ISO_DATE_TIME);
        return webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/api/hechos")
                .queryParam("desde", fechaUltimaActualizacionStr)  //    /api/hechos?desde=fecha
                .build()
            )
            .retrieve()
            .bodyToFlux(HechoFuenteDTO.class)
            .collectList();
    }


    public void guardarCambios(Hecho hecho) {
        hechosRepository.save(hecho);
    }


    public void modificarHecho(Hecho viejo, Hecho nuevo) {
        viejo.setTitulo(nuevo.getTitulo());
        viejo.setDescripcion(nuevo.getDescripcion());
        viejo.setCategoria(nuevo.getCategoria());
        viejo.setContenidosMultimedia(nuevo.getContenidosMultimedia());
        viejo.setLugarAcontecimiento(nuevo.getLugarAcontecimiento());
        viejo.setFechaHecho(nuevo.getFechaHecho());
        viejo.setFechaUltimaActualizacion(LocalDateTime.now());
        viejo.setRevisado(nuevo.isRevisado());
        viejo.setDepartamento(nuevo.getDepartamento());
    }


    //TODO: revisar si es funcional esto asincronico
    @Async
    public void eliminarHechoEnLasFuentes(Hecho hecho){ // Llamada no bloqueante a otra API
        webClients.get(hecho.getOrigen())
            .delete()
            .uri("api/hechos/{id}", hecho.getIdExterno().getIdExterno())
            .retrieve()
            .toBodilessEntity()
            .doOnSuccess(response -> System.out.println("Eliminación remota exitosa"))
            .doOnError(error -> System.err.println("Error en la eliminación remota"))
            .subscribe();

        applicationEventPublisher.publishEvent(new HechoEliminadoEvent(hecho));
    }
}

