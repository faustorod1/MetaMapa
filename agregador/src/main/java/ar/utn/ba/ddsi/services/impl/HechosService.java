package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.commons.Coordenada;
import ar.utn.ba.ddsi.commons.DivisorEnLotes;
import ar.utn.ba.ddsi.models.dtos.RestPage;
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
import ar.utn.ba.ddsi.services.IGeorefService;
import ar.utn.ba.ddsi.services.IHechosService;
import ar.utn.ba.ddsi.services.internal.WebApiCallerService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.ParameterizedTypeReference;
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
    private IGeorefService georefService;

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

    public Page<HechoOutputDTO> obtenerPorContribuyente(Long contribuyenteId, Map<String, String> params, Pageable pageable) {
        Specification<Hecho> specFiltros = HechoSpecs.porFiltros(params);
        Specification<Hecho> specContribuyente = HechoSpecs.porContribuyente(contribuyenteId);
        Specification<Hecho> specFinal = Specification.where(specContribuyente).and(specFiltros);
        Page<Hecho> paginaHechos = hechosRepository.findAll(specFinal, pageable);

        return paginaHechos.map(HechoOutputDTO::fromEntity);
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
        int BATCH_SIZE = 100;

        webClients.forEach(((tipoDeFuente, webClient) -> {
            try {
                LocalDateTime desde = fechaUltimaActualizacionFuentes.get(tipoDeFuente);
                LocalDateTime inicioProceso = LocalDateTime.now();

                int page = 0;
                boolean hayMasPaginas = true;

                while (hayMasPaginas) {
                    RestPage<HechoFuenteDTO> pagina = getFromFuentePaginado(webClient, desde, page, BATCH_SIZE);

                    if (pagina != null && !pagina.getContent().isEmpty()) {
                        procesarLoteDeHechos(pagina.getContent());
                        logger.info("Fuente {}: Procesada página {} con {} hechos.", tipoDeFuente, page, pagina.getNumberOfElements());
                    }

                    if (pagina == null || pagina.isLast() || pagina.getContent().isEmpty()) {
                        hayMasPaginas = false;
                    } else {
                        page++;
                    }

                    fechaUltimaActualizacionFuentes.put(tipoDeFuente, inicioProceso);
                    logger.info("Sincronización finalizada exitosamente con fuente: {}", tipoDeFuente);
                }
            } catch (Exception e) {
                logger.error("Error crítico sincronizando fuente {}: {}", tipoDeFuente, e.getMessage());
            }
        }));

    }

    private void procesarLoteDeHechos(List<HechoFuenteDTO> dtos) {
        if (dtos == null || dtos.isEmpty()) return;

        List<Fuente> fuentes = fuentesRepository.findAll();

        List<Hecho> hechosDelLote = new ArrayList<>(dtos.stream().map(dto -> {
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
                Fuente guardada = fuentesRepository.save(nuevaFuente);
                fuentes.add(guardada);
                return guardada;
            });
            hecho.getIdExterno().setFuente(fuente);
            return hecho;
        }).toList());

        List<IdExterno> idsDelLote = hechosDelLote.stream().map(Hecho::getIdExterno).toList();
        List<Hecho> hechosExistentesEnDB = hechosRepository.findAllByIdExternoIn(idsDelLote);

        normalizarCategoria(hechosDelLote);
        normalizarUbicacion(hechosDelLote);

        for (int i = 0; i < hechosDelLote.size(); i++) {
            Hecho hechoNormalizado = hechosDelLote.get(i);

            Hecho viejo = hechosExistentesEnDB.stream()
                    .filter(h -> h.getIdExterno().equals(hechoNormalizado.getIdExterno()))
                    .findFirst().orElse(null);

            if (viejo != null) {
                modificarHecho(viejo, hechoNormalizado);
                hechosDelLote.set(i, viejo);
            } else {
                hechoNormalizado.setFechaUltimaActualizacion(LocalDateTime.now());
            }
        }

        hechosRepository.saveAll(hechosDelLote);
        applicationEventPublisher.publishEvent(new HechosModificadosEvent(hechosDelLote));
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

            listaDeCategorias.add(hecho.getCategoria());
            categoriaRepository.save(hecho.getCategoria());
        });
    }


    @Override
    public void normalizarUbicacion(List<Hecho> hechos){
        List<Hecho> requierenGeorref = hechos.stream()
                .filter(h -> h.getLugarAcontecimiento() != null)
                .toList();
        if (requierenGeorref.isEmpty()) {
            return;
        }

        List<Coordenada> coordenadas = requierenGeorref.stream()
                .map(Hecho::getLugarAcontecimiento)
                .distinct()
                .toList();

        Map<Coordenada, Departamento> mapaDepartamentos = georefService.obtenerDepartamentos(coordenadas);

        requierenGeorref.forEach(hecho -> {
            Departamento depto = mapaDepartamentos.get(hecho.getLugarAcontecimiento());
            if (depto != null) {
                hecho.setDepartamento(depto);
            }
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
            logger.error("Error crítico al sincronizar hechos MetaMapa: {}", e.getMessage());
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

    private RestPage<HechoFuenteDTO> getFromFuentePaginado(WebClient webClient, LocalDateTime desde, int page, int size) {
        String fechaUltimaActualizacionStr = desde.format(DateTimeFormatter.ISO_DATE_TIME);

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("desde", fechaUltimaActualizacionStr);
        queryParams.put("page", String.valueOf(page));
        queryParams.put("size", String.valueOf(size));

        return webApiCallerService.getPageWithAuth(
                webClient,
                "/api/hechos",
                queryParams,
                new ParameterizedTypeReference<RestPage<HechoFuenteDTO>>() {}
        );
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
        viejo.setEtiquetas(nuevo.getEtiquetas());
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


    @Override
    public Integer pedirCantidadDeHechosEnElSistema(){
        return hechosRepository.countByEliminadoFalse();
    }

    @Override
    public HechoOutputDTO buscarUltimoHechoCargado() {
        Hecho ultimoHecho = hechosRepository.findTopByOrderByFechaDeCargaDesc();

        if (ultimoHecho == null) {
            return null; // Devuelve null si no se encontró ningún hecho.
        }

        return HechoOutputDTO.fromEntity(ultimoHecho);
    }
}

