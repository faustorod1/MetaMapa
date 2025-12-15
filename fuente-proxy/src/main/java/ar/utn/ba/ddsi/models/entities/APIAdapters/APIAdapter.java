package ar.utn.ba.ddsi.models.entities.APIAdapters;

import ar.utn.ba.ddsi.models.entities.Hecho;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

public abstract class APIAdapter {
    abstract public Mono<List<Hecho>> getHechos();

    public List<Hecho> getHechosDesde(LocalDateTime desde) {
        return getHechos().map(list ->
                list.stream().filter(hecho ->
                        hecho.getFechaUltimaActualizacion().isAfter(desde)
                ).toList()
        ).block();
    }

    abstract public Hecho getById(String id);
}
