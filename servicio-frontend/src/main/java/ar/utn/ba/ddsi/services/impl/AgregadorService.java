package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.models.dto.input.*;
import ar.utn.ba.ddsi.models.dto.output.HechoOutputDTO;
import ar.utn.ba.ddsi.models.dto.output.SolicitudDeEliminacionOutputDTO;
import ar.utn.ba.ddsi.services.IAgregadorService;
import ar.utn.ba.ddsi.services.internal.WebApiCallerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AgregadorService implements IAgregadorService {
  WebClient agregadorWebClient;
  String agregadorBaseUrl;
  WebApiCallerService webApiCallerService;

  public AgregadorService(@Value("${servicio.agregador.api.base-url}") String agregadorUrl, WebApiCallerService webApiCallerService) {
    agregadorBaseUrl = agregadorUrl;
    agregadorWebClient = WebClient.builder().baseUrl(agregadorBaseUrl).build();
    this.webApiCallerService = webApiCallerService;
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

   public void solicitarEliminacion(SolicitudDeEliminacionOutputDTO solicitud) {
      webApiCallerService.post(
              agregadorBaseUrl + "/api/solicitudes",
                solicitud,
                String.class);
   }

}
