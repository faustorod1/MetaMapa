package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.models.dto.input.HechoDTO;
import ar.utn.ba.ddsi.models.dto.output.HechoOutputDTO;
import ar.utn.ba.ddsi.models.entities.Hecho;
import ar.utn.ba.ddsi.services.IHechosService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class HechosService implements IHechosService {
  WebClient agregadorWebClient;
  WebClient dinamicaWebClient;


  public HechosService(@Value("${servicio.agregador.api.base-url}") String agregadorBaseUrl, @Value("${fuente.dinamica.api.base-url}") String fuenteDinamicaUrl) {
    agregadorWebClient = WebClient.builder().baseUrl(agregadorBaseUrl).build();
    dinamicaWebClient = WebClient.builder().baseUrl(fuenteDinamicaUrl).build();
  }

  public List<HechoDTO> buscarTodos() {
    return agregadorWebClient.get()
        .uri("/api/hechos")
        .retrieve()
        .bodyToFlux(HechoDTO.class)
        .collectList()
        .block();
  }

  /*
  public HechoDTO cargarHecho(){
    return dinamicaWebClient.post()
  }
  */


}
