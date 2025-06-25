package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.models.dtos.external.ContribuyenteDTO;
import ar.utn.ba.ddsi.models.dtos.output.HechoOutputDTO;
import ar.utn.ba.ddsi.models.dtos.external.HechoFuenteDTO;
import ar.utn.ba.ddsi.models.entities.*;
import ar.utn.ba.ddsi.models.repositories.IHechosRepository;
import ar.utn.ba.ddsi.services.IHechosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriUtils;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class HechosService implements IHechosService {
    private IHechosRepository hechosRepository;

    private Map<OrigenHecho, WebClient> webClients = new HashMap<OrigenHecho, WebClient>();
    private LocalDateTime fechaUltimaActualizacion = LocalDate.parse("01/01/1000", DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay();


    @Autowired
    public HechosService(IHechosRepository hechosRepository, @Value("${fuente.estatica.api.base-url}") String fuenteEstaticaApiBaseUrl, @Value("${fuente.dinamica.api.base-url}") String fuenteDinamicaApiBaseUrl, @Value("${fuente.proxy.api.base-url}") String fuenteProxyApiBaseUrl) {
        this.hechosRepository = hechosRepository;
        this.webClients.put(OrigenHecho.DATASET, WebClient.builder().baseUrl(fuenteEstaticaApiBaseUrl).build());
        this.webClients.put(OrigenHecho.CONTRIBUYENTE, WebClient.builder().baseUrl(fuenteDinamicaApiBaseUrl).build());
        this.webClients.put(OrigenHecho.PROXY, WebClient.builder().baseUrl(fuenteProxyApiBaseUrl).build());
    }

    // --- Métodos expuestos al controller -------------------------------------------------------------------------------

    // TODO:
    // Si en el agregador tenemos siempre los hechos de fuentes MetaMapa, al sumarle los que pedimos en tiempo
    // real, quedan repetidos
    @Override
    public Mono<List<HechoOutputDTO>> buscarTodos(Criterio criterio){
        if (criterio == null) {
            criterio = new Criterio(); // Por defecto, solo filtra los eliminados
        }
        Mono<List<Hecho>> hechosLocales = Mono.fromCallable(hechosRepository::findAll);
        Mono<List<Hecho>> hechosMetaMapa = this.getFromMetaMapa();

        Mono<List<Hecho>> todos = Mono.zip(hechosLocales, hechosMetaMapa)
                .map(tuple ->
                        Stream.concat(tuple.getT1().stream(), tuple.getT2().stream()).toList()
                ).map(criterio::aplicarA);

        return todos.map(list -> list.stream().map(this::hechoOutputDTO).toList());
    }


    // ---- Métodos de trabajo interno -------------------------------------------------------------------------------

    @Override
    public Hecho obtenerPorId(Long id){
        return hechosRepository.findById(id);
    }

    @Override
    public Mono<Void> actualizarHechos(){
        Mono<List<Hecho>> monoEstatica = crearMonoPeticionHechos(webClients.get(OrigenHecho.DATASET));
        Mono<List<Hecho>> monoDinamica = crearMonoPeticionHechos(webClients.get(OrigenHecho.CONTRIBUYENTE));
        Mono<List<Hecho>> monoProxy = crearMonoPeticionHechos(webClients.get(OrigenHecho.PROXY));

        Mono<Void> mono = Mono.zip(monoEstatica, monoDinamica, monoProxy)
                .doOnNext(tupla -> {
                    hechosRepository.saveAll(tupla.getT1());
                    hechosRepository.saveAll(tupla.getT2());
                    hechosRepository.saveAll(tupla.getT3());
                })
                .then();

        fechaUltimaActualizacion = LocalDateTime.now();
        return mono;
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
                .map(this::hechoFromHechoFuenteDTO)
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
                .map(this::hechoFromHechoFuenteDTO)
                .collectList();
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
    }
    
    // ---- Conversiones DTO -------------------------------------------------------------------------------

    public HechoOutputDTO hechoOutputDTO(Hecho hecho) {
        HechoOutputDTO dto = new HechoOutputDTO();

        dto.setId(hecho.getId());
        dto.setTitulo(hecho.getTitulo());
        dto.setDescripcion(hecho.getDescripcion());
        dto.setCategoria(hecho.getCategoria());
        dto.setContenidoMultimedia(hecho.getContenidoMultimedia());
        dto.setOrigen(hecho.getOrigen());
        dto.setLugarAcontecimiento(hecho.getLugarAcontecimiento());
        dto.setFechaHecho(hecho.getFechaHecho());
        dto.setFechaDeCarga(hecho.getFechaDeCarga());
        dto.setIdExterno(hecho.getIdExterno());
        if (hecho.getContribuyente() != null) dto.setContribuyente(hecho.getContribuyente().getId());
        //dto.setSolicitudesDeEliminacion(new ArrayList<>());
//        dto.setSolicitudesDeEliminacion(
//            hecho.getSolicitudesDeEliminacion()
//                .stream()
//                .map(ISolicitudesService::solicititudDeEliminacionToDTO)
//                .toList()
//        );
        dto.setEtiquetas(
                hecho.getEtiquetas()
                        .stream()
                        .map(Etiqueta::nombre)
                        .collect(Collectors.toCollection(HashSet::new))
        );
        return dto;
    }

    private Hecho hechoFromHechoFuenteDTO(HechoFuenteDTO dto) {
        Contribuyente contribuyente = null;
        if (dto.getContribuyente() != null) {
            contribuyente = contribuyenteFromContribuyenteDTO(dto.getContribuyente());
        }

        Hecho hecho = Hecho.builder()
                .idExterno(dto.getId())
                .titulo(dto.getTitulo())
                .descripcion(dto.getDescripcion())
                .categoria(dto.getCategoria())
                .contenidoMultimedia(dto.getContenidoMultimedia())
                .origen(dto.getOrigen())
                .lugarAcontecimiento(dto.getLugarAcontecimiento())
                .fechaHecho(dto.getFechaHecho())
                .fechaDeCarga(dto.getFechaDeCarga())
                .contribuyente(contribuyente)
                .solicitudesDeEliminacion(dto.getSolicitudesDeEliminacion()) // Cambiar
                .build();

        return hecho;
    }

    private Contribuyente contribuyenteFromContribuyenteDTO(ContribuyenteDTO dto) {
        return new Contribuyente(dto.getId(), dto.getNombre(), dto.getApellido(), LocalDate.parse(dto.getFechaDeNacimiento(),DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }


}

