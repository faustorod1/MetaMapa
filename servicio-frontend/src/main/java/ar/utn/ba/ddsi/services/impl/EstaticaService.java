package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.services.IEstaticaService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;
import java.util.List;

@Service
public class EstaticaService implements IEstaticaService {
    WebClient estaticaWebClient;

    public EstaticaService(@Value("${fuente.estatica.api.base-url}") String fuenteEstaticaUrl) {
          this.estaticaWebClient = WebClient.builder().baseUrl(fuenteEstaticaUrl).build();
    }

    public void importarCSVs(List<MultipartFile> archivos) {
        estaticaWebClient.post()
                .uri("/importar")
                .bodyValue(archivos)
                .retrieve()
                .toBodilessEntity()
                .block();

    }
}
