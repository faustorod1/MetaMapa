package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.commons.Coordenada;
import ar.utn.ba.ddsi.models.dtos.apigob.GeorefRequestMultipleDTO;
import ar.utn.ba.ddsi.models.dtos.apigob.GeorefRequestDTO;
import ar.utn.ba.ddsi.models.dtos.apigob.GeorreferenciacionDTO;
import ar.utn.ba.ddsi.models.dtos.apigob.ResultadoGeoDTO;
import ar.utn.ba.ddsi.models.dtos.external.ContribuyenteDTO;
import ar.utn.ba.ddsi.models.dtos.output.CategoriaDTO;
import ar.utn.ba.ddsi.models.dtos.output.HechoOutputDTO;
import ar.utn.ba.ddsi.models.dtos.external.HechoFuenteDTO;
import ar.utn.ba.ddsi.models.entities.*;
import ar.utn.ba.ddsi.models.entities.ubicacion.Departamento;
import ar.utn.ba.ddsi.models.repositories.ICategoriaRepository;
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
import org.springframework.web.util.UriUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public HechosService(IHechosRepository hechosRepository, @Value("${fuente.estatica.api.base-url}") String fuenteEstaticaApiBaseUrl, @Value("${fuente.dinamica.api.base-url}") String fuenteDinamicaApiBaseUrl, @Value("${fuente.proxy.api.base-url}") String fuenteProxyApiBaseUrl, @Value("${georef.api.base-url}") String georefApiBaseUrl, ApplicationEventPublisher applicationEventPublisher) {
        this.hechosRepository = hechosRepository;
        this.webClients.put(OrigenHecho.DATASET, WebClient.builder().baseUrl(fuenteEstaticaApiBaseUrl).build());
        this.webClients.put(OrigenHecho.CONTRIBUYENTE, WebClient.builder().baseUrl(fuenteDinamicaApiBaseUrl).build());
        this.webClients.put(OrigenHecho.PROXY, WebClient.builder().baseUrl(fuenteProxyApiBaseUrl).build());
        this.webClientGeoref = WebClient.builder().baseUrl(georefApiBaseUrl).build();
        this.applicationEventPublisher = applicationEventPublisher;
    }

    // --- Métodos expuestos al controller -------------------------------------------------------------------------------

    // TODO:
    // Si en el agregador tenemos siempre los hechos de fuentes MetaMapa, al sumarle los que pedimos en tiempo
    // real, quedan repetidos
    @Override
    public Mono<List<HechoOutputDTO>> buscarTodos(Map<String, String> params) {
        Criterio criterio = new Criterio(params);

        Mono<List<Hecho>> hechosLocales = Mono.fromCallable(hechosRepository::findAll);
        Mono<List<Hecho>> hechosMetaMapa = this.getFromMetaMapa();

        Mono<List<Hecho>> todos = Mono.zip(hechosLocales, hechosMetaMapa)
                .map(tuple ->
                        Stream.concat(tuple.getT1().stream(), tuple.getT2().stream()).toList()
                ).map(criterio::aplicarA);

        return todos.map(list -> list.stream().map(HechoOutputDTO::fromEntity).toList());
    }


    // ---- Métodos de trabajo interno -------------------------------------------------------------------------------

    @Override
    public Hecho obtenerPorId(Long id){
        return hechosRepository.findById(id).orElse(null);
    }

    @Override
    public Mono<Void> actualizarHechos(){
        Mono<List<Hecho>> monoEstatica = crearMonoPeticionHechos(webClients.get(OrigenHecho.DATASET));
        Mono<List<Hecho>> monoDinamica = crearMonoPeticionHechos(webClients.get(OrigenHecho.CONTRIBUYENTE));
        Mono<List<Hecho>> monoProxy = crearMonoPeticionHechos(webClients.get(OrigenHecho.PROXY));

        Mono<Void> mono = Mono.zip(monoEstatica, monoDinamica, monoProxy)
                .publishOn(Schedulers.boundedElastic())
                .flatMap(tupla -> {
                    List<Hecho> hechos = new ArrayList<>(tupla.getT1());

                    hechos.addAll(tupla.getT2());
                    hechos.addAll(tupla.getT3());

                    List<String> ids = hechos.stream().map(Hecho::getIdExterno).toList();
                    List<Hecho> hechosAModificar = hechosRepository.findAllByIdExternoIn(ids);

                    normalizarCategoria(hechos);
                    return normalizarUbicacion(hechos).map(hechosNormalizados -> {
                                // Para cada hecho normalizado, si ya existía en el agregador, lo actualiza
                                for (int i = 0; i < hechosNormalizados.size(); i++) {
                                    Hecho hechoNormalizado = hechosNormalizados.get(i);
                                    Hecho viejo = hechosAModificar.stream()
                                            .filter(h -> h.getIdExterno().equals(hechoNormalizado.getIdExterno()))
                                            .findFirst().orElse(null);
                                    if (viejo != null) {
                                        modificarHecho(viejo, hechoNormalizado);
                                        hechosNormalizados.set(i, viejo);
                                    } else {
                                        hechoNormalizado.setFechaUltimaActualizacion(LocalDateTime.now());
                                    }
                                }

                                hechosRepository.saveAll(hechosNormalizados);

                                return hechosNormalizados;
                            })
                            .then();
                }).then();

        fechaUltimaActualizacion = LocalDateTime.now();

        
        // Código necesario para activar el evento
        List<Hecho> todosLosHechos = Mono.zip(monoEstatica, monoDinamica, monoProxy)
            .map(tuple -> Stream.of(tuple.getT1(), tuple.getT2(), tuple.getT3())
                .flatMap(List::stream)
                .toList())
                .block();
        applicationEventPublisher.publishEvent(new HechosModificadosEvent(todosLosHechos));

        return mono;
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
    public Mono<List<Hecho>> normalizarUbicacion(List<Hecho> hechos){
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

        List<List<Coordenada>> lotes = dividirEnLotes(coordenadas, 300);

        return Flux.fromIterable(lotes)
                .concatMap(this::georreferenciacionInversa)
                .collectList()
                .map(listaDeMapas -> {
                    // Junta las respuestas de las peticiones en un solo Map
                    Map<Coordenada, Departamento> combinados = new HashMap<>();
                    listaDeMapas.forEach(combinados::putAll);

                    combinados.forEach((coordenada, departamento) ->
                            hechosPorCoordenada.get(coordenada).forEach(hechoAModificar ->
                                    hechoAModificar.setDepartamento(departamento)
                            )
                    );
                    return hechos;
            });
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
    public Mono<List<Hecho>> getFromMetaMapa() {
        return webClients.get(OrigenHecho.PROXY)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/hechos/metamapaInstance")     // TODO: Ver si nos conviene pedir una fuente MetaMapa específica
                        .build()
                )
                .retrieve()
                .bodyToFlux(HechoFuenteDTO.class)
                .map(HechoFuenteDTO::toEntity)
                .collectList();
    }


    private Mono<List<Hecho>> crearMonoPeticionHechos(WebClient webClient) {
        String fechaUltimaActualizacionStr = fechaUltimaActualizacion.format(DateTimeFormatter.ISO_DATE_TIME);
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/hechos")
                        .queryParam("desde", fechaUltimaActualizacionStr)  //    /api/hechos?desde=fecha
                        .build()
                )
                .retrieve()
                .bodyToFlux(HechoFuenteDTO.class)
                .map(HechoFuenteDTO::toEntity)
                .collectList();
    }

    // TODO: Está bien esto?
    public void guardarCambios(Hecho hecho) {
        hechosRepository.save(hecho);
    }

    // No lo guarda, solo actualiza sus atributos
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
        // Para escapar los ":" del id
        String idExternoEscapado = UriUtils.encodePathSegment(hecho.getIdExterno(), StandardCharsets.UTF_8);

        webClients.get(hecho.getOrigen())
                .delete()
                .uri("api/hechos/{id}", idExternoEscapado)
                .retrieve()
                .toBodilessEntity()
                .doOnSuccess(response -> System.out.println("Eliminación remota exitosa"))
                .doOnError(error -> System.err.println("Error en la eliminación remota"))
                .subscribe();
        
        applicationEventPublisher.publishEvent(new HechoEliminadoEvent(hecho));
    }


    private <T> List<List<T>> dividirEnLotes(List <T> lista, Integer tamLote) {
        List<List<T>> lotes = new ArrayList<>();
        final int tamLista = lista.size();
        int contador = 0;
        while (contador < tamLista) {
            int fin = Math.min(contador + tamLote, tamLista);
            List<T> lote = lista.subList(contador, fin);
            lotes.add(lote);
            contador += tamLote;
        }
        return lotes;
    }
}

