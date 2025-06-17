package ar.utn.ba.ddsi.models.entities;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class API {
    private Long id;
    private String url;
    // private adapter;     // TODO: ¿tenemos un adapter por tipo de API, o un adapter solo para todo el sistema?

    //public List<Hecho> getHechos() {
        // return adapter.getHechos(url) //algo así
    //}
}
