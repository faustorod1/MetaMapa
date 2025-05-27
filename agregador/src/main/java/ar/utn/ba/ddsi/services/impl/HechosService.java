package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.models.dtos.external.ContribuyenteDTO;
import ar.utn.ba.ddsi.models.dtos.output.HechoOutputDTO;
import ar.utn.ba.ddsi.models.dtos.external.HechoFuenteDTO;
import ar.utn.ba.ddsi.models.entities.*;
import ar.utn.ba.ddsi.models.repositories.IHechosRepository;
import ar.utn.ba.ddsi.services.IHechosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.stream.Collectors;

@Service
public class HechosService implements IHechosService {
    private IHechosRepository hechosRepository;
    private WebClient estaticaWebClient;
    private WebClient dinamicaWebClient;
    private WebClient proxyWebClient;

    private LocalDateTime fechaUltimaActualizacion = LocalDate.parse("01/01/1000", DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay();


    @Autowired
    public HechosService(IHechosRepository hechosRepository, @Value("${fuente.estatica.api.base-url}") String fuenteEstaticaApiBaseUrl, @Value("${fuente.dinamica.api.base-url}") String fuenteDinamicaApiBaseUrl, @Value("${fuente.proxy.api.base-url}") String fuenteProxyApiBaseUrl) {
        this.hechosRepository = hechosRepository;
        this.estaticaWebClient = WebClient.builder().baseUrl(fuenteEstaticaApiBaseUrl).build();
        this.dinamicaWebClient = WebClient.builder().baseUrl(fuenteDinamicaApiBaseUrl).build();
        this.proxyWebClient = WebClient.builder().baseUrl(fuenteProxyApiBaseUrl).build();
    }

    @Override
    public List<HechoOutputDTO> buscarTodos(Criterio criterio){
        if (criterio == null) {
            criterio = new Criterio(); // Por defecto, solo filtra los eliminados
        }
        List<Hecho> hechosTotales = new ArrayList<>(hechosRepository.findAll());

        List<Hecho> hechosMetamapa = this.pedirHechosMetamapa().block();
        hechosTotales.addAll(hechosMetamapa);


        List<Hecho> hechosFiltrados = criterio.aplicarA(hechosTotales);

        return hechosFiltrados
                .stream()
                .map(this::hechoOutputDTO)
                .toList();
    }

    @Override
    public Hecho obtenerPorId(Long id){
        return hechosRepository.findById(id);
    }

    private HechoOutputDTO hechoOutputDTO(Hecho hecho) {
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

    @Override
    public Mono<Void> actualizarHechos(){
        this.dinamicaWebClient.get()
                .uri("/tu-endpoint") // o la ruta que estés usando
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(System.out::println)
                .then();

        Mono<List<Hecho>> monoEstatica = crearMonoPeticionHechos(estaticaWebClient);
        Mono<List<Hecho>> monoDinamica = crearMonoPeticionHechos(dinamicaWebClient);
        Mono<List<Hecho>> monoProxy = crearMonoPeticionHechos(proxyWebClient);

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

    private Mono<List<Hecho>> pedirHechosMetamapa() {
        return proxyWebClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/api/hechos/metamapaInstance/a")
                .build()
            )
            .retrieve()
            .bodyToFlux(HechoFuenteDTO.class)
            .map(this::hechoFromHechoFuenteDTO)
            .collectList()
            .map(ArrayList::new);
    }
}

