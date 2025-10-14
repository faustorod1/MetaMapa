package ar.utn.ba.ddsi.models.entities.APIAdapters.impl;

import ar.utn.ba.ddsi.commons.Coordenada;
import ar.utn.ba.ddsi.models.dtos.externals.APICatedra.APICatedraLogInDTO;
import ar.utn.ba.ddsi.models.dtos.externals.APICatedra.APICatedraLoginDataDTO;
import ar.utn.ba.ddsi.models.dtos.externals.APICatedra.APICatedraHechoDTO;
import ar.utn.ba.ddsi.models.dtos.externals.APICatedra.APICatedraResponseDto;
import ar.utn.ba.ddsi.models.entities.Hecho;
import ar.utn.ba.ddsi.models.entities.APIAdapters.IAPIAdapter;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

public class APIAdapterFuenteCatedra implements IAPIAdapter {
    private WebClient webClient;
    private String token;
    private String email;
    private String password;

    public APIAdapterFuenteCatedra(String email, String password){
        this.email = email;
        this.password = password;
        webClient = WebClient.builder().baseUrl("https://api-ddsi.disilab.ar/public/api").build();
    }

//    @PostConstruct
//    public void inicializar(){
//        this.token = this.iniciarSesion();
//        System.out.println("TOKEN: " + this.token);
//    }


    private void iniciarSesion(){
        this.token = webClient.post().uri(uriBuilder -> uriBuilder.path("/login")
        .queryParam("email", email)
        .queryParam("password", password)
        .build())
        .retrieve()
        .bodyToMono(APICatedraLogInDTO.class)
        .map(APICatedraLogInDTO::getData)
        .map(APICatedraLoginDataDTO::getAccess_token)
        .block();
    }

    @Override
    public Mono<List<Hecho>> getHechos() {
        if (token == null) {
            iniciarSesion();
        }
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

    private Hecho externalToHecho (APICatedraHechoDTO dto) {
        return Hecho.builder()
                .id(dto.getId())
                .fechaHecho(dto.getFecha_hecho())
                .fechaDeCarga(dto.getCreated_at())
                .categoria(dto.getCategoria())
                .descripcion(dto.getDescripcion())
                .titulo(dto.getTitulo())
                .fechaUltimaActualizacion(dto.getUpdated_at())
                .lugarAcontecimiento(new Coordenada(dto.getLatitud(), dto.getLongitud()))
                .build();
    }
}