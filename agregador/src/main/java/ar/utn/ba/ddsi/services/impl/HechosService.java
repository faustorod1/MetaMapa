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
import ar.utn.ba.ddsi.models.specifications.HechoSpecs;
import ar.utn.ba.ddsi.services.IHechosService;
import ar.utn.ba.ddsi.services.internal.WebApiCallerService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HechosService implements IHechosService {
    private final ApplicationEventPublisher applicationEventPublisher;
    private IHechosRepository hechosRepository;

    private final Map<TipoDeFuente, WebClient> webClients = new HashMap<TipoDeFuente, WebClient>();
    private final Map<TipoDeFuente, LocalDateTime> fechaUltimaActualizacionFuentes = new HashMap<>();
    private final WebClient webClientGeoref;

    private static final Logger logger = LoggerFactory.getLogger(HechosService.class);

    @Autowired
    private ICategoriaRepository categoriaRepository;
    @Autowired
    private IDepartamentosRepository departamentosRepository;
    @Autowired
    private IFuentesRepository fuentesRepository;

    @Autowired
    private WebApiCallerService webApiCallerService;

    @Autowired
    public HechosService(IHechosRepository hechosRepository, @Value("${fuente.estatica.api.base-url}") String fuenteEstaticaApiBaseUrl, @Value("${fuente.dinamica.api.base-url}") String fuenteDinamicaApiBaseUrl, @Value("${fuente.proxy.api.base-url}") String fuenteProxyApiBaseUrl, @Value("${georef.api.base-url}") String georefApiBaseUrl, ApplicationEventPublisher applicationEventPublisher, WebApiCallerService webApiCallerService) {
        this.hechosRepository = hechosRepository;
        this.webClients.put(TipoDeFuente.ESTATICA, WebClient.builder().baseUrl(fuenteEstaticaApiBaseUrl).build());
        this.webClients.put(TipoDeFuente.DINAMICA, WebClient.builder().baseUrl(fuenteDinamicaApiBaseUrl).build());
        this.webClients.put(TipoDeFuente.PROXY, WebClient.builder().baseUrl(fuenteProxyApiBaseUrl).build());
        this.webClientGeoref = WebClient.builder().baseUrl(georefApiBaseUrl).build();
        this.applicationEventPublisher = applicationEventPublisher;
        this.webApiCallerService = webApiCallerService;
        this.fechaUltimaActualizacionFuentes.put(TipoDeFuente.ESTATICA, LocalDate.parse("01/01/1999", DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay());
        this.fechaUltimaActualizacionFuentes.put(TipoDeFuente.DINAMICA, LocalDate.parse("01/01/1999", DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay());
        this.fechaUltimaActualizacionFuentes.put(TipoDeFuente.PROXY, LocalDate.parse("01/01/1999", DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay());
    }

    // --- Métodos expuestos al controller -------------------------------------------------------------------------------

    @Override
    public Page<HechoOutputDTO> buscarTodos(Map<String, String> params, Pageable pageable) {
        Specification<Hecho> spec = HechoSpecs.porFiltros(params);
        Page<Hecho> paginaLocal = hechosRepository.findAll(spec, pageable);

        if (paginaLocal.isEmpty()) {
            return Page.empty(pageable);
        }

        List<Hecho> hechosDeLaPagina = new ArrayList<>(paginaLocal.getContent());
        List<Hecho> actualizadosConMetamapa = actualizarListaConHechosMetamapa(hechosDeLaPagina);

        Page<Hecho> paginaFinal = new PageImpl<>(actualizadosConMetamapa, pageable, paginaLocal.getTotalElements());

        return paginaFinal.map(HechoOutputDTO::fromEntity);
    }

    @Override
    public HechoOutputDTO buscarHecho(Long id){
        return HechoOutputDTO.fromEntity(obtenerPorId(id));
    }

    @Override
    public HechoOutputDTO buscarHechoNoEliminado(Long id){
        return HechoOutputDTO.fromEntity(obtenerNoEliminadoPorId(id));
    }

    // -------------------------------------------------------- Métodos de trabajo interno ---------------------------------------------------------------------------//
    @Override
    public Hecho obtenerPorId(Long id){
        return hechosRepository.findById(id).orElse(null);
    }


    @Override
    public Page<Hecho> obtenerPorColeccion(Long id, Map<String, String> params, Pageable pageable) {
        String modo = params.getOrDefault("modo", "curada");
        boolean traerConsensuados = !"irrestricta".equalsIgnoreCase(modo);

        Specification<Hecho> specFiltros = HechoSpecs.porFiltros(params);
        Specification<Hecho> specColeccion = HechoSpecs.pertenecienteAColeccion(id, traerConsensuados);
        Specification<Hecho> specFinal = Specification.where(specColeccion).and(specFiltros);
        Page<Hecho> paginaHechos = hechosRepository.findAll(specFinal, pageable);

        List<Hecho> actualizadosConMetamapa = actualizarListaConHechosMetamapa(paginaHechos.getContent());

        Page<Hecho> pagina = new PageImpl<>(actualizadosConMetamapa, pageable, paginaHechos.getTotalElements());
        return pagina;
    }


    @Override
    public Hecho obtenerNoEliminadoPorId(Long id){
        Hecho hecho = hechosRepository.findById(id).orElse(null);
        if (hecho == null || hecho.isEliminado()){
            throw new EntityNotFoundException("El Hecho con ID: " + id + " está marcado como eliminado.");
        }else{
            return hecho;
        }
    }

    @Override
    public void actualizarHechos(){

        // Ejecuta las peticiones a las fuentes y junta los resultados
        List<HechoFuenteDTO> hechosFuente = new ArrayList<>();
        webClients.forEach((key, value) -> {
            try {
                LocalDateTime desde = fechaUltimaActualizacionFuentes.get(key);
                List<HechoFuenteDTO> hechosFuenteDTO = getFromFuente(value, desde);
                if (hechosFuenteDTO != null && !hechosFuenteDTO.isEmpty()) {
                    logger.info("Se encontraron {} hechos de la fuente: {}", hechosFuenteDTO.size(), key);
                    hechosFuente.addAll(hechosFuenteDTO);
                } else {
                    logger.warn("No se recibieron hechos de la fuente: {}", key);
                }
                fechaUltimaActualizacionFuentes.put(key, LocalDateTime.now());
            } catch (Exception e) {
                logger.error("Error al obtener hechos de la fuente {}: {}", key, e.getMessage());
                //e.printStackTrace();
            }
        });

        // Carga las fuentes del repo y los setea en los hechos obtenidos
        List<Fuente> fuentes = fuentesRepository.findAll();

        List<Hecho> hechos = new ArrayList<>(hechosFuente.stream().map(dto -> {
            Hecho hecho = dto.toEntity();
            Fuente fuente = fuentes.stream().filter(f -> {
                boolean fuenteCoincide = f.getTipoDeFuente().name().equals(dto.getTipoDeFuente());
                if (f.getSubfuenteId() == null || dto.getSubfuenteId() == null) {
                    return fuenteCoincide;
                }
                return fuenteCoincide && f.getSubfuenteId().equals(dto.getSubfuenteId());
            }).findFirst().orElseGet(() -> {
                Fuente nuevaFuente = new Fuente();
                nuevaFuente.setTipoDeFuente(TipoDeFuente.valueOf(dto.getTipoDeFuente()));
                nuevaFuente.setSubfuenteId(dto.getSubfuenteId());

                fuentesRepository.save(nuevaFuente);
                fuentes.add(nuevaFuente);
                return nuevaFuente;
            });

            hecho.getIdExterno().setFuente(fuente);
            return hecho;
        }).toList());

        // Busca los hechos que comparten ID Externo con los actualizados, pues van a tener que modificarlos
        List<IdExterno> ids = hechos.stream().map(Hecho::getIdExterno).toList();
        List<Hecho> hechosAModificar = hechosRepository.findAllByIdExternoIn(ids);

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
            for (Categoria categoria : listaDeCategorias) {
                if (categoria.esSimilarSegunLevenshtein(categoriaOriginal)) {
                    hecho.setCategoria(categoria);
                    return;
                }
            }

            categoriaRepository.save(hecho.getCategoria());
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


    // Reemplaza de la colección (ya persistida localmente) con los MetaMapa obtenidos recién
    // Si hay algún hecho de fuente MetaMapa que NO teníamos en el agregador (porque se agregó a esa
    // fuente MetaMapa después de la última actualización), ese hecho NO se incluye entre los hechos
    // que devolvemos acá, ya que no sabemos si debería pertenecer a la colección.
    @Override
    public List<Hecho> actualizarListaConHechosMetamapa(List<Hecho> hechosLocales) {
        Map<IdExterno, Hecho> hechosPorId = hechosLocales.stream()
                .collect(Collectors.toMap(Hecho::getIdExterno, h -> h));

        try {
            List<Hecho> hechosMetaMapa = this.getFromMetaMapa();

            for (Hecho hechoMetamapa : hechosMetaMapa) {
                if (hechosPorId.containsKey(hechoMetamapa.getIdExterno())) {
                    hechoMetamapa.setId(hechosPorId.get(hechoMetamapa.getIdExterno()).getId());
                    hechosPorId.put(hechoMetamapa.getIdExterno(), hechoMetamapa);
                }
            }
            return new ArrayList<>(hechosPorId.values());
        } catch (WebClientException e) {
            e.printStackTrace();
        }
        return hechosLocales;
    }

    @Override
    public List<Hecho> getFromMetaMapa() {
        return webApiCallerService
                .getListWithAuth(
                        webClients.get(TipoDeFuente.PROXY),
                        "/api/hechos/metamapaInstance",
                        null,
                        HechoFuenteDTO.class
                )
                .stream()
                .map(HechoFuenteDTO::toEntity)
                .toList();
    }

    private List<HechoFuenteDTO> getFromFuente(WebClient webClient, LocalDateTime desde) {
        String fechaUltimaActualizacionStr = desde.format(DateTimeFormatter.ISO_DATE_TIME);
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("desde", fechaUltimaActualizacionStr);
        List<HechoFuenteDTO> hechos = webApiCallerService.getListWithAuth(webClient, "/api/hechos", queryParams, HechoFuenteDTO.class);
        return hechos;
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

    public List<HechoOutputDTO> buscarHechoDe(Long contribuyenteId) {
        return hechosRepository.findByContribuyenteId(contribuyenteId).stream()
                .filter(hecho -> !hecho.isEliminado())
                .map(HechoOutputDTO::fromEntity).toList();
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

    @Override
    public List<HechoOutputDTO> buscarHechos(LocalDateTime fecha, Integer cantidad_obtener) {
        Pageable pageable = PageRequest.of(0, cantidad_obtener);

        List<Hecho> hechosEncontrados = hechosRepository.findByFechaDeCargaLessThanEqualOrderByFechaDeCargaDesc(fecha, pageable);

        return hechosEncontrados.stream()
            .map(HechoOutputDTO::fromEntity).toList();
    }

    @Override
    public List<Long> buscarIdsExternosDinamica(){
        return hechosRepository.findAll().stream().filter(hecho -> !hecho.isEliminado() && hecho.getOrigen()
                .equals(OrigenHecho.CONTRIBUYENTE))
                .map(hecho -> hecho.getIdExterno().getIdExterno()).toList();
    }

    @Override
    public HechoOutputDTO buscarHechoDinamica (Long id_externo){
        return hechosRepository.findAll().stream()
                .filter(hecho -> hecho.getIdExterno().getIdExterno().equals(id_externo))
                .filter(hecho -> hecho.getIdExterno().getFuente().getTipoDeFuente().equals(TipoDeFuente.DINAMICA))
                .findFirst().map(HechoOutputDTO::fromEntity).orElse(null);
    }
}

