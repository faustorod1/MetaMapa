package ar.utn.ba.ddsi.services.impl;

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
        List<Hecho> hechosFiltrados = criterio.aplicarA(hechosRepository.findAll());

        return hechosFiltrados
                .stream()
                .map(this::hechoOutputDTO)
                .toList();
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
        dto.setSolicitudesDeEliminacion(hecho.getSolicitudesDeEliminacion());
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
                .contribuyente(dto.getContribuyente())
                .solicitudesDeEliminacion(dto.getSolicitudesDeEliminacion()) // Cambiar
                .build();

            return hecho;
        }


}

