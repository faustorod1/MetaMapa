package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.exceptions.BadCodeException;
import ar.utn.ba.ddsi.exceptions.UsuarioExistenteException;
import ar.utn.ba.ddsi.models.dto.external.AuthResponseDTO;
import ar.utn.ba.ddsi.models.dto.external.UserRolesDTO;
import ar.utn.ba.ddsi.models.dto.input.ColeccionDTO;
import ar.utn.ba.ddsi.models.dto.input.HechoDTO;
import ar.utn.ba.ddsi.services.IRootService;
import ar.utn.ba.ddsi.services.internal.WebApiCallerService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Map;

@Service
public class RootService implements IRootService {
    private final ObjectMapper objectMapper;
    String agregadorBaseUrl;
    WebClient agregadorWebClient;
    WebClient usuariosWebClient;
    WebApiCallerService webApiCallerService;
    private final String authServiceUrl;

    public RootService(@Value("${servicio.agregador.api.base-url}") String agregadorBaseUrl, @Value("${servicio.usuarios.api.base-url}") String servicioDeUsuarios, WebApiCallerService webApiCallerService, ObjectMapper objectMapper) {
        this.agregadorBaseUrl = agregadorBaseUrl;
        agregadorWebClient = WebClient.builder().baseUrl(agregadorBaseUrl).build();
        usuariosWebClient = WebClient.builder().baseUrl(servicioDeUsuarios).build();
        this.authServiceUrl = servicioDeUsuarios;
        this.webApiCallerService = webApiCallerService;
        this.objectMapper = objectMapper;
    }

    public AuthResponseDTO login(String email, String password) {
        try {

            AuthResponseDTO response = usuariosWebClient
                    .post()
                    .uri("/api/auth")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Map.of(
                            "email", email,
                            "password", password
                    ))
                    .retrieve()
                    .bodyToMono(AuthResponseDTO.class)
                    .block();

            if (response != null) {
                webApiCallerService.updateTokensInSession(response.getAccessToken(), response.getRefreshToken());
            }

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

    public AuthResponseDTO registrar(String nombre, String apellido, String email, String password, String repetedPassword, String code) {

        try {
            return usuariosWebClient.post()
                    .uri("/api/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Map.of("nombre", nombre,
                            "apellido", apellido,
                            "email", email,
                            "password", password,
                            "repetedPassword", repetedPassword,
                            "code", code))
                    .retrieve()
                    .bodyToMono(AuthResponseDTO.class)
                    .block();
       } catch (WebClientResponseException e) {
            String respuestaJsonError = e.getResponseBodyAsString();
            String mensajeLimpio = extraerMensajeDelJson(respuestaJsonError);

            if (e.getStatusCode() == HttpStatus.CONFLICT) {
                throw new UsuarioExistenteException("El correo electrónico ingresado ya está registrado.");
            }

            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new BadCodeException("El código de administrador proporcionado es incorrecto.");
            }

            throw new RuntimeException("Error en el servicio de autenticación (" + e.getStatusCode() + "): " + mensajeLimpio, e);
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al conectar con auth: " + e.getMessage(), e);
        }
    }

    @Override
    public List<HechoDTO> getHechosDestacados(Integer cantidadHechos) {
        return this.getListaDestacadas(HechoDTO.class, "api/hechos/destacados/" + cantidadHechos.toString());
    }

    @Override
    public List<ColeccionDTO> getColeccionesDestacadas(Integer cantidadColecciones){
        return this.getListaDestacadas(ColeccionDTO.class, "api/colecciones/destacadas/" + cantidadColecciones.toString());
    }

    private <T> java.util.List<T> getListaDestacadas(Class<T> responseType, String url){
        try{
            List<T> response = agregadorWebClient.get().uri(url).retrieve().bodyToFlux(responseType).collectList().block();
            return response;
        }catch (WebClientResponseException e) {
            throw new RuntimeException("Error en el servicio" + e.getStatusCode());
        }catch (Exception e) {
            throw new RuntimeException("Error inesperado al conectar" + e.getMessage(), e);
        }
    }

    private String extraerMensajeDelJson(String json) {
        try {
            JsonNode root = objectMapper.readTree(json);
            return root.path("error").asText();
        } catch (Exception e) {
            return "Ocurrió un error al procesar la solicitud.";
        }
    }
}




