package ar.utn.ba.ddsi.services;

import ar.utn.ba.ddsi.models.dtos.input.ColeccionInputDTO;
import ar.utn.ba.ddsi.models.dtos.input.CriterioInputDTO;
import ar.utn.ba.ddsi.models.dtos.output.ColeccionOutputDTO;
import ar.utn.ba.ddsi.models.dtos.output.HechoOutputDTO;
import ar.utn.ba.ddsi.models.entities.Coleccion;
import ar.utn.ba.ddsi.models.entities.Criterio;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IColeccionesService {
    List<ColeccionOutputDTO> buscarTodos();
    Mono<List<HechoOutputDTO>> buscarHechosPorColeccion(String identificador);
    ColeccionOutputDTO crearColeccion(ColeccionInputDTO input);
    ColeccionOutputDTO updateColeccion(ColeccionInputDTO input);
    ColeccionOutputDTO updateCriterio(String identificador, CriterioInputDTO criterioInputDTO);
    void eliminarColeccion (String identificador);
}
