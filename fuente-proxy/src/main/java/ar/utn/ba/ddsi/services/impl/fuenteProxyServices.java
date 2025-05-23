package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.models.dtos.externals.HechoFuenteApiCatedraResponseDto;
import ar.utn.ba.ddsi.models.dtos.externals.HechoDTO;
import ar.utn.ba.ddsi.models.dtos.outputs.HechoOutputDTO;
import ar.utn.ba.ddsi.models.entities.Categoria;
import ar.utn.ba.ddsi.models.entities.Coordenada;
import ar.utn.ba.ddsi.services.ifuenteProxyServices;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class fuenteProxyServices implements ifuenteProxyServices {
  private WebClient webClient;
  private String token;

  public fuenteProxyServices(WebClient.Builder webClientBuilder) {
    this.token = "rY3j0CD1b4hpJBNWwZvJkva2NhsGEukeS2pFQkjE2yMBmk6sdlGQ5ATQkpYo";
    this.webClient = webClientBuilder.baseUrl("https://api-ddsi.disilab.ar/public/api").build();
  }

  @Override
  public Mono<HechoDTO> getById(Long id) {//pedimos un hecho
    return webClient
            .get()
            .uri("/desastres/{id}", id)
            .header("Authorization", "Bearer " + this.token)
            .retrieve()
            .bodyToMono(HechoDTO.class);
  }

  public Mono<List<HechoDTO>> consumirMetamapa(String baseUrl) {
    WebClient metamapa = WebClient.builder().baseUrl(baseUrl).build();

    return metamapa
            .get()
            .uri("/hechos")
            .retrieve()
            .bodyToMono(HechoFuenteApiCatedraResponseDto.class)
            .map(HechoFuenteApiCatedraResponseDto::getData);
  }

  @Override
  public Mono<List<HechoDTO>> getAll() {
    return Flux.range(1, 100)
            .flatMap(page -> webClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/desastres")
                            .queryParam("page", page)
                            .queryParam("per_page", 100)
                            .build())
                    .header("Authorization", "Bearer " + this.token)
                    .retrieve()
                    .bodyToMono(HechoFuenteApiCatedraResponseDto.class)
                    .map(HechoFuenteApiCatedraResponseDto::getData))
            .flatMap(Flux::fromIterable)
            .collectList();
  }

  public HechoOutputDTO externalToOutput (HechoDTO hechoDTO) {
    return HechoOutputDTO.builder()
            .id(hechoDTO.getId())
            .titulo(hechoDTO.getTitulo())
            .descripcion(hechoDTO.getDescripcion())
            .categoria(new Categoria(hechoDTO.getCategoria()))
            .coordenada(new Coordenada(hechoDTO.getLatitud(), hechoDTO.getLongitud()))
            .fechaHecho(LocalDate.parse(hechoDTO.getFecha_hecho(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSX")))
            .fechaDeCarga(LocalDateTime.parse(hechoDTO.getCreated_at(),DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSX")))
            .lastUpdate(LocalDateTime.parse(hechoDTO.getUpdated_at(),DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSX")))
            .build();
  }
}