package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.models.dtos.outputs.HechoOutputDTO;
import ar.utn.ba.ddsi.services.ifuenteProxyServices;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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
  public Mono<List<HechoOutputDTO>> getAll() {
    return webClient
        .get()
        .uri("/desastres")
        .header("Authorization", "Bearer " + this.token)
        .retrieve()
        .bodyToMono(HechoOutputDTO.class)
        .map(HechoOutputDTO::getProducts);
  }
}
