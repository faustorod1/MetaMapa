package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.models.dtos.output.HechoOutputDTO;
import ar.utn.ba.ddsi.models.dtos.external.FuenteHechoResponseDTO;
import ar.utn.ba.ddsi.models.dtos.external.HechoFuenteDTO;
import ar.utn.ba.ddsi.models.entities.*;
import ar.utn.ba.ddsi.models.repositories.IHechosRepository;
import ar.utn.ba.ddsi.services.IHechosService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.HashSet;
import java.util.stream.Collectors;

@Service
public class HechosService implements IHechosService {
    private IHechosRepository hechosRepository;
    private WebClient estaticaWebClient;



    @Autowired
    public HechosService(IHechosRepository hechosRepository, @Value("${fuente.estatica.api.base-url}") String fuenteEstaticaApiBaseUrl) {
        this.hechosRepository = hechosRepository;
        this.estaticaWebClient = WebClient.builder().baseUrl(fuenteEstaticaApiBaseUrl).build();
        System.out.println("---------" + fuenteEstaticaApiBaseUrl);
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
        if (hecho.getCategoria() != null) dto.setCategoria(hecho.getCategoria().getNombre());
        if (hecho.getContenidoMultimedia() != null) dto.setContenidoMultimedia(hecho.getContenidoMultimedia().getPathImagen());
        if (hecho.getOrigen() != null) dto.setOrigen(hecho.getOrigen());
        if (hecho.getLugarAcontecimiento() != null) dto.setLugarAcontecimiento(hecho.getLugarAcontecimiento().comoArray());
        dto.setFechaHecho(hecho.getFechaHecho());
        dto.setFechaDeCarga(hecho.getFechaDeCarga());
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
    public Mono<Void> actualizarHechos() {
        return estaticaWebClient.get()
                .uri("/api/hechos")
                .retrieve()
                .bodyToFlux(HechoFuenteDTO.class)
                .map(this::hechoFromHechoFuenteDTO)
                .collectList()
                .doOnNext(hechosRepository::saveAll)
                .then();
    }


    private Hecho hechoFromHechoFuenteDTO(HechoFuenteDTO dto) {

        Hecho hecho = Hecho.builder()
                .id(dto.getId())
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

