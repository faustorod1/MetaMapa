package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.services.IRootService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;

public class RootService implements IRootService {
    WebClient agregadorWebClient;

    public RootService(@Value("${agregador.api.base-url}") String agregadorBaseUrl) {
        agregadorWebClient = WebClient.builder().baseUrl(agregadorBaseUrl).build();
    }

    /* Idea para login: conectarse con un endpoint del agregador, y que el se encargue de revisar en la BD si existe ese usuario.
    public boolean loginExitoso(LoginDatosDTO){
    return agregador.post
        .uri("/api/login")
        .bodyValue(LoginDatosDTO)
        ...
    }

    */

}
