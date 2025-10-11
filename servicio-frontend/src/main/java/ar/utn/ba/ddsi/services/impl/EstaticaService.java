package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.services.IEstaticaService;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;
import java.util.List;
import java.util.Objects;

@Service
public class EstaticaService implements IEstaticaService {
    WebClient estaticaWebClient;

    public EstaticaService(@Value("${fuente.estatica.api.base-url}") String fuenteEstaticaUrl) {
          this.estaticaWebClient = WebClient.builder().baseUrl(fuenteEstaticaUrl).build();
    }

    public void importarCSVs(List<MultipartFile> archivos) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        for (MultipartFile archivo : archivos) {
            builder.part("files", archivo.getResource())
                    .filename(Objects.requireNonNull(archivo.getOriginalFilename()));
        }


        estaticaWebClient.post()
                .uri("/api/datasets")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
