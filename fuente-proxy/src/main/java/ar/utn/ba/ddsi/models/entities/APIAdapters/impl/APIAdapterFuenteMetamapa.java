package ar.utn.ba.ddsi.models.entities.APIAdapters.impl;

import ar.utn.ba.ddsi.commons.Coordenada;
import ar.utn.ba.ddsi.models.dtos.externals.HechoExternalMetamapaDTO;
import ar.utn.ba.ddsi.models.dtos.externals.HechosExternalMetamapaDTO;
import ar.utn.ba.ddsi.models.entities.APIAdapters.IAPIAdapter;
import ar.utn.ba.ddsi.models.entities.Hecho;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class APIAdapterFuenteMetamapa implements IAPIAdapter{
    private WebClient webClient;

    public APIAdapterFuenteMetamapa(String url){
        webClient = WebClient.builder().baseUrl(url).build();
    }

    @Override
    public Mono<List<Hecho>> getHechos() {
        return webClient
                .get()
                .uri("/hechos")
                .retrieve()
                .bodyToMono(HechosExternalMetamapaDTO.class)
                .map(HechosExternalMetamapaDTO::getHechos)
                .map(list -> list.stream().map(this::externalMetamapaToHecho).toList());
    }

    private Hecho externalMetamapaToHecho (HechoExternalMetamapaDTO dto) {
        return Hecho.builder()
                .id(dto.getId())
                .titulo(dto.getTitulo())
                .descripcion(dto.getDescripcion())
                .fechaDeCarga(LocalDateTime.parse(dto.getFechaDeCarga()))
                .fechaHecho(LocalDate.parse(dto.getFechaHecho()))
                .lugarAcontecimiento(new Coordenada(dto.getLatitud(), dto.getLongitud()))
                .fechaUltimaActualizacion(LocalDateTime.parse(dto.getFechaUltimaActualizacion()))
                .build();
    }
}
