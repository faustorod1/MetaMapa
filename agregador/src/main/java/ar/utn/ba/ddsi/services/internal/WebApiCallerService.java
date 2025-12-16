package ar.utn.ba.ddsi.services.internal;


import ar.utn.ba.ddsi.models.dtos.external.SystemTokenResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
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
    public <T> T getWithAuth(WebClient webClient, String uri, Map<String, String> queryParams, Class<T> responseType) {
        try {
            return this.executeGetRequest(webClient, uri, queryParams, responseType);
        } catch (Exception e) {
            this.loginToSystem();
            return this.executeGetRequest(webClient, uri, queryParams, responseType);
        }
    }

    /**
     * Ejecuta una llamada HTTP GET con el token system
     */
    public <T> List<T> getListWithAuth(WebClient webClient,String uri, Map<String, String> queryParams, Class<T> responseType) {
        try {
            return this.executeGetListRequest(webClient, uri, queryParams, responseType);
        } catch (Exception e) {
            this.loginToSystem();
            return this.executeGetListRequest(webClient, uri, queryParams, responseType);
        }
    }

    /**
     * Ejecuta una llamada HTTP GET paginada con el token system
     */
    public <T> T getPageWithAuth(WebClient webClient, String uri, Map<String, String> queryParams, ParameterizedTypeReference<T> responseType) {
        try {
            return this.executeGetRequestPage(webClient, uri, queryParams, responseType);
        } catch (Exception e) {
            this.loginToSystem();
            return this.executeGetRequestPage(webClient, uri, queryParams, responseType);
        }
    }

    /**
     * Ejecuta una llamada HTTP POST que retorna una LISTA (Ideal para el Batch de Proxy)
     * @param body El objeto que va en el body (ej: List<SolicitudBatchDTO>)
     */
    public <T> List<T> postListWithAuth(WebClient webClient, String uri, Object body, Class<T> responseType) {
        try {
            return this.executePostListRequest(webClient, uri, body, responseType);
        } catch (Exception e) {
            this.loginToSystem(); // Retry logic: Si falla (token vencido), reloguea y reintenta
            return this.executePostListRequest(webClient, uri, body, responseType);
        }
    }
    /**
     * Ejecuta una llamada HTTP POST simple que retorna un solo objeto
     */
    public <T> T postWithAuth(WebClient webClient, String uri, Object body, Class<T> responseType) {
        try {
            return this.executePostRequest(webClient, uri, body, responseType);
        } catch (Exception e) {
            this.loginToSystem();
            return this.executePostRequest(webClient, uri, body, responseType);
        }
    }


    private <T> T executeGetRequestPage(WebClient webClient, String uri, Map<String, String> queryParams, ParameterizedTypeReference<T> responseType) {
        return webClient
                .get()
                .uri(uriBuilder -> {
                    if (queryParams != null) {
                        queryParams.forEach(uriBuilder::queryParam);
                    }
                    return uriBuilder.path(uri).build();
                })
                .header("Authorization", "Bearer " + systemAccessToken)
                .retrieve()
                .bodyToMono(responseType)
                .block();
    }


    private <T> T executeGetRequest(WebClient webClient,String uri, Map<String, String> queryParams, Class<T> responseType) {
        return webClient
                .get()
                .uri(uriBuilder -> {

                    if (queryParams != null) {
                        queryParams.forEach(uriBuilder::queryParam);
                    }
                    return uriBuilder.path(uri).build();
                })
                .header("Authorization", "Bearer " + systemAccessToken)
                .retrieve()
                .bodyToMono(responseType)
                .block();
    }

    private <T> List<T> executeGetListRequest(WebClient webClient, String uri, Map<String, String> queryParams, Class<T> responseType) {
        List<T> lista = webClient
                .get()
                .uri(uriBuilder -> {
                    if (queryParams != null) {
                        queryParams.forEach(uriBuilder::queryParam);
                    }
                    return uriBuilder.path(uri).build();
                })
                .header("Authorization", "Bearer " + systemAccessToken)
                .retrieve()
                .bodyToFlux(responseType)
                .collectList()
                .block();
        return lista;
    }

    private <T> List<T> executePostListRequest(WebClient webClient, String uri, Object body, Class<T> responseType) {
        return webClient
                .post()
                .uri(uri)
                .header("Authorization", "Bearer " + systemAccessToken)
                .bodyValue(body)
                .retrieve()
                .bodyToFlux(responseType)
                .collectList()
                .block();
    }

    private <T> T executePostRequest(WebClient webClient, String uri, Object body, Class<T> responseType) {
        return webClient
                .post()
                .uri(uri)
                .header("Authorization", "Bearer " + systemAccessToken)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(responseType)
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