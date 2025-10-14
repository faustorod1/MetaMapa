package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.models.dto.input.*;
import ar.utn.ba.ddsi.models.dto.output.HechoOutputDTO;
import ar.utn.ba.ddsi.services.IAgregadorService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class AgregadorService implements IAgregadorService {
  WebClient agregadorWebClient;

  public AgregadorService(@Value("${servicio.agregador.api.base-url}") String agregadorBaseUrl) {
    agregadorWebClient = WebClient.builder().baseUrl(agregadorBaseUrl).build();
  }

  public List<HechoDTO> buscarHechos() {
    return agregadorWebClient.get()
        .uri("/api/hechos")
        .retrieve()
        .bodyToFlux(HechoDTO.class)
        .collectList()
        .block();
  }

  public List<FuenteDTO> buscarFuentes(){
    return agregadorWebClient.get()
            .uri("/api/colecciones/fuentes")
            .retrieve()
            .bodyToFlux(FuenteDTO.class)
            .collectList()
            .block();
  }

  public HechoDTO pedirHecho(Long id) {
    return agregadorWebClient.get()
            .uri("/api/hechos/{id}", id)
            .retrieve()
            .bodyToMono(HechoDTO.class)
            .block();
   }


}
