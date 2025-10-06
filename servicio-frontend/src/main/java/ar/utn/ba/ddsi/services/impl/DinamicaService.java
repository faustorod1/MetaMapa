package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.models.dto.output.HechoOutputDTO;
import ar.utn.ba.ddsi.services.IDinamicaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class DinamicaService implements IDinamicaService {
    WebClient dinamicaWebClient;

    public DinamicaService(@Value("${fuente.dinamica.api.base-url}") String fuenteDinamicaUrl) {
        dinamicaWebClient = WebClient.builder().baseUrl(fuenteDinamicaUrl).build();
    }

    public void cargarHecho(HechoOutputDTO hecho){
        dinamicaWebClient.post()
            .uri("/api/hechos")
            .bodyValue(hecho)
            .retrieve()
            .toBodilessEntity()
            .block();
    }
}
