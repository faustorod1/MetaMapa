package ar.utn.ba.ddsi.models.entities.APIAdapters;

import ar.utn.ba.ddsi.models.dtos.externals.HechoExternalDTO;
import ar.utn.ba.ddsi.models.dtos.outputs.HechoOutputDTO;
import ar.utn.ba.ddsi.models.entities.Hecho;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

public interface IAPIAdapter {
    Mono<List<Hecho>> getHechos();
}
