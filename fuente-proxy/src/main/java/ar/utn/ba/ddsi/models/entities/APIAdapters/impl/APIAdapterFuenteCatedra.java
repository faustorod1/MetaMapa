package ar.utn.ba.ddsi.models.entities.APIAdapters.impl;

import ar.utn.ba.ddsi.commons.Coordenada;
import ar.utn.ba.ddsi.models.dtos.externals.HechoExternalDTO;
import ar.utn.ba.ddsi.models.dtos.externals.APICatedraResponseDto;
import ar.utn.ba.ddsi.models.entities.Categoria;
import ar.utn.ba.ddsi.models.entities.Hecho;
import ar.utn.ba.ddsi.models.entities.APIAdapters.IAPIAdapter;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class APIAdapterFuenteCatedra implements IAPIAdapter {
    private WebClient webClient;
    private String token;
    private String email;
    private String password;


    public APIAdapterFuenteCatedra(String url){
        email = "ddsi@gmail.com";
        password = "password";
        webClient = WebClient.builder().baseUrl(url).build();

        this.token = this.iniciarSesion();
    }

    private String iniciarSesion(){
        return webClient.post().uri(uriBuilder -> uriBuilder.path("/login")
                .queryParam("email", email)
                .queryParam("password", password)
                .build())
                .retrieve()
                .toString();
    }

    @Override
    public Mono<List<Hecho>> getHechos() {
        return Flux.range(1, 100)
                .parallel()
                .runOn(Schedulers.parallel())
                .flatMap(page -> webClient.get()
                        .uri(uriBuilder -> uriBuilder.path("/desastres")
                                .queryParam("page", page)
                                .queryParam("per_page", 100)
                                .build())
                        .header("Authorization", "Bearer " + this.token)
                        .retrieve()
                        .bodyToMono(APICatedraResponseDto.class)
                        .map(APICatedraResponseDto::getData))
                .sequential()
                .flatMap(Flux::fromIterable)
                .map(this::externalToHecho)
                .collectList();
    }

    public Hecho externalToHecho (HechoExternalDTO dto) {
        return Hecho.builder()
                .id(dto.getId())
                .fechaHecho(LocalDate.parse(dto.getFecha_hecho()))
                .fechaDeCarga(LocalDateTime.parse(dto.getCreated_at()))
                .categoria(new Categoria(dto.getCategoria()))
                .descripcion(dto.getDescripcion())
                .titulo(dto.getTitulo())
                .fechaUltimaActualizacion(LocalDateTime.parse(dto.getUpdated_at()))
                .lugarAcontecimiento(new Coordenada(dto.getLatitud(), dto.getLongitud()))
                .build();
    }
}