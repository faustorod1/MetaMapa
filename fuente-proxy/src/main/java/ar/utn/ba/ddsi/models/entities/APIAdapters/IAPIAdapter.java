package ar.utn.ba.ddsi.models.entities.APIAdapters;

import ar.utn.ba.ddsi.models.entities.Hecho;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IAPIAdapter {
    Mono<List<Hecho>> getHechos();
}
