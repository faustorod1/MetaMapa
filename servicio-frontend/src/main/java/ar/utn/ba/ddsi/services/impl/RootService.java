package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.models.dto.external.AuthResponseDTO;
import ar.utn.ba.ddsi.models.dto.external.UserRolesDTO;
import ar.utn.ba.ddsi.services.IRootService;
import ar.utn.ba.ddsi.services.internal.WebApiCallerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;

@Service
public class RootService implements IRootService {
    WebClient agregadorWebClient;
    WebClient usuariosWebClient;
    WebApiCallerService webApiCallerService;
    private final String authServiceUrl;

    public RootService(@Value("${servicio.agregador.api.base-url}") String agregadorBaseUrl, @Value("${servicio.usuarios.api.base-url}") String servicioDeUsuarios, WebApiCallerService webApiCallerService) {
        agregadorWebClient = WebClient.builder().baseUrl(agregadorBaseUrl).build();
        usuariosWebClient = WebClient.builder().baseUrl(servicioDeUsuarios).build();
        this.authServiceUrl = servicioDeUsuarios;
        this.webApiCallerService = webApiCallerService;
    }

    public AuthResponseDTO login(String email, String password) {
        try {
            AuthResponseDTO response = usuariosWebClient
                    .post()
                    .uri("/api/auth")
                    .bodyValue(Map.of(
                            "email", email,
                            "password", password
                    ))
                    .retrieve()
                    .bodyToMono(AuthResponseDTO.class)
                    .block();
            return response;
        } catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return null;
            }
            throw new RuntimeException("Error en el servicio de autenticación: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error de conexión con el servicio de autenticación: " + e.getMessage(), e);
        }
    }

    public UserRolesDTO getRole(String accessToken) {
        try {
            UserRolesDTO response = webApiCallerService.getWithAuth(
                    authServiceUrl + "/api/auth/roles",
                    accessToken,
                    UserRolesDTO.class
            );
            return response;
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener roles: " + e.getMessage(), e);
        }
    }

    public AuthResponseDTO registrar(String nombre, String apellido, String email, String password, String repetedPassword) {

        try {
            return usuariosWebClient.post()
                    .uri("/api/auth/register")
                    .bodyValue(Map.of("nombre", nombre,
                            "apellido", apellido,
                            "email", email,
                            "password", password,
                            "repetedPassword", repetedPassword))
                    .retrieve()
                    .bodyToMono(AuthResponseDTO.class)
                    .block();
        } catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return null;
            }
            throw new RuntimeException("Error en el servicio de autenticación: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error de conexión con el servicio de autenticación: " + e.getMessage(), e);
        }

    }
}




