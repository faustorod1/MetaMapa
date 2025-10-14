package ar.utn.ba.ddsi.services.internal;


import ar.utn.ba.ddsi.models.dtos.external.SystemTokenResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WebApiCallerService {

    private final WebClient webClientAuth;

    private final String serviceClientId;
    private final String serviceClientSecret;

    private String systemAccessToken;

    public WebApiCallerService(@Value("${auth.service.url}") String authServiceUrl, @Value("${service.client.id}") String serviceClientId, @Value("${service.client.secret}") String serviceClientSecret) {
        this.webClientAuth = WebClient.builder().baseUrl(authServiceUrl).build();
        this.serviceClientId = serviceClientId;
        this.serviceClientSecret = serviceClientSecret;
    }

    /**
     * Ejecuta una llamada HTTP GET con el token system
     */
    public <T> T getWithAuth(WebClient webClient, Map<String, String> queryParams, Class<T> responseType) {
        try {
            return this.executeGetRequest(webClient, queryParams, responseType);
        } catch (Exception e) {
            this.loginToSystem();
            return this.executeGetRequest(webClient, queryParams, responseType);
        }
    }

    /**
     * Ejecuta una llamada HTTP GET con el token system
     */
    public <T> List<T> getListWithAuth(WebClient webClient, Map<String, String> queryParams, Class<T> responseType) {
        try {
            return this.executeGetListRequest(webClient, queryParams, responseType);
        } catch (Exception e) {
            this.loginToSystem();
            return this.executeGetListRequest(webClient, queryParams, responseType);
        }
    }


    private <T> T executeGetRequest(WebClient webClient, Map<String, String> queryParams, Class<T> responseType) {
        return webClient
                .get()
                .uri(uriBuilder -> {
                    // Itera sobre el Map y agrega cada par clave-valor como un parámetro
                    if (queryParams != null) {
                        queryParams.forEach(uriBuilder::queryParam);
                    }
                    return uriBuilder.build();
                })
                .header("Authorization", "Bearer " + systemAccessToken)
                .retrieve()
                .bodyToMono(responseType)
                .block();
    }

    private <T> List<T> executeGetListRequest(WebClient webClient, Map<String, String> queryParams, Class<T> responseType) {
        return webClient
                .get()
                .uri(uriBuilder -> {
                    if (queryParams != null) {
                        queryParams.forEach(uriBuilder::queryParam);
                    }
                    return uriBuilder.build();
                })
                .header("Authorization", "Bearer " + systemAccessToken)
                .retrieve()
                .bodyToFlux(responseType)
                .collectList()
                .block();
    }

    private void loginToSystem() {
        SystemTokenResponseDTO response = webClientAuth
                .post()
                .uri("/api/internal/auth/token")
                .bodyValue(Map.of(
                "clientId", serviceClientId,
                "clientSecret", serviceClientSecret
                ))
                .retrieve()
                .bodyToMono(SystemTokenResponseDTO.class)
                .block();
        systemAccessToken = response.getAccessToken();
    }
}