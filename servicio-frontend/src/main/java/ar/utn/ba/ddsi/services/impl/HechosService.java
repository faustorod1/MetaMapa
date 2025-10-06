package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.models.dto.input.HechoDTO;
import ar.utn.ba.ddsi.models.dto.output.HechoOutputDTO;
import ar.utn.ba.ddsi.services.IHechosService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class HechosService implements IHechosService {
  WebClient agregadorWebClient;

  public HechosService(@Value("${servicio.agregador.api.base-url}") String agregadorBaseUrl) {
    agregadorWebClient = WebClient.builder().baseUrl(agregadorBaseUrl).build();
  }

  public List<HechoDTO> buscarTodos() {
    return agregadorWebClient.get()
        .uri("/api/hechos")
        .retrieve()
        .bodyToFlux(HechoDTO.class)
        .collectList()
        .block();
  }





}
