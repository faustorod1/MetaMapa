package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.models.dto.output.HechoOutputDTO;
import ar.utn.ba.ddsi.services.IDinamicaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class DinamicaService implements IDinamicaService {
    WebClient dinamicaWebClient;

    public DinamicaService(@Value("${fuente.dinamica.api.base-url}") String fuenteDinamicaUrl) {
        dinamicaWebClient = WebClient.builder().baseUrl(fuenteDinamicaUrl).build();
    }

    public void cargarHecho(HechoOutputDTO hecho, List<MultipartFile> imagenes){
        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        // Agregamos el JSON del hecho
        builder.part("hecho", hecho)
                .header("Content-Type", "application/json");

        // Agregamos cada archivo
        if (imagenes != null) {
            for (MultipartFile img : imagenes) {
                builder.part("contenidosMultimedia", img.getResource());
            }
        }

        dinamicaWebClient.post()
                .uri("/api/hechos")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
