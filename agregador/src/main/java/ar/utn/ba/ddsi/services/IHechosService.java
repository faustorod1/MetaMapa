package ar.utn.ba.ddsi.services;

import ar.utn.ba.ddsi.models.dtos.external.HechoFuenteDTO;
import ar.utn.ba.ddsi.models.dtos.output.HechoOutputDTO;
import ar.utn.ba.ddsi.models.entities.Criterio;
import ar.utn.ba.ddsi.models.entities.Hecho;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IHechosService {
    Mono<List<HechoOutputDTO>> buscarTodos(Criterio criterio);
    Hecho obtenerPorId(Long id);

    Mono<List<Hecho>> getFromMetaMapa();
    Mono<Void> actualizarHechos();
    HechoOutputDTO hechoOutputDTO(Hecho hecho);
}
