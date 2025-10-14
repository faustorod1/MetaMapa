package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.services.IEstaticaService;
import ar.utn.ba.ddsi.services.internal.WebApiCallerService;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;
import java.util.List;
import java.util.Objects;

@Service
public class EstaticaService implements IEstaticaService {
    WebClient estaticaWebClient;
    String estaticaBaseUrl;
    WebApiCallerService webApiCallerService;

    public EstaticaService(@Value("${fuente.estatica.api.base-url}") String fuenteEstaticaUrl, WebApiCallerService webApiCallerService) {
        estaticaBaseUrl = fuenteEstaticaUrl;
        this.estaticaWebClient = WebClient.builder().baseUrl(fuenteEstaticaUrl).build();
        this.webApiCallerService = webApiCallerService;
    }

    public void importarCSVs(List<MultipartFile> archivos) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        for (MultipartFile archivo : archivos) {
            builder.part("files", archivo.getResource())
                    .filename(Objects.requireNonNull(archivo.getOriginalFilename())).contentType(MediaType.MULTIPART_FORM_DATA);
        }
        MultiValueMap<String, HttpEntity<?>> multipartBody = builder.build();

        webApiCallerService.postMultipart(
                estaticaBaseUrl + "/api/datasets",
                multipartBody,
                String.class
        );

//        webApiCallerService.executeWithTokenRetry(accessToken ->
//                estaticaWebClient
//                        .post()
//                        .uri("/api/datasets")
//                        .contentType(MediaType.MULTIPART_FORM_DATA)
//                        .header("Authorization", "Bearer " + accessToken)
//                        .body(BodyInserters.fromMultipartData(body))
//                        .retrieve()
//                        .bodyToMono(String.class)
//                        .block()

    }
}
