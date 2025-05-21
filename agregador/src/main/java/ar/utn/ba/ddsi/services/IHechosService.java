package ar.utn.ba.ddsi.services;

import ar.utn.ba.ddsi.models.dtos.output.HechoOutputDTO;
import ar.utn.ba.ddsi.models.entities.Criterio;
import ar.utn.ba.ddsi.models.entities.Hecho;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IHechosService {
    List<HechoOutputDTO> buscarTodos(Criterio criterio);

    public Mono<List<HechoOutputDTO>> actualizarHechos();
}
