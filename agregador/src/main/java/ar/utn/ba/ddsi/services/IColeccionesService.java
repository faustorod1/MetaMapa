package ar.utn.ba.ddsi.services;

import ar.utn.ba.ddsi.models.dtos.input.ColeccionInputDTO;
import ar.utn.ba.ddsi.models.dtos.input.CriterioInputDTO;
import ar.utn.ba.ddsi.models.dtos.output.ColeccionOutputDTO;
import ar.utn.ba.ddsi.models.dtos.output.HechoOutputDTO;
import ar.utn.ba.ddsi.models.entities.Coleccion;
import ar.utn.ba.ddsi.models.entities.Criterio;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface IColeccionesService {
    List<ColeccionOutputDTO> buscarTodos();
    Mono<List<HechoOutputDTO>> buscarHechosPorColeccion(String identificador, String modo, Map<String, String> params);
    ColeccionOutputDTO crearColeccion(ColeccionInputDTO input);
    ColeccionOutputDTO updateColeccion(ColeccionInputDTO input);
    ColeccionOutputDTO updateCriterio(String identificador, CriterioInputDTO criterioInputDTO);
    void eliminarColeccion (String identificador);
    ColeccionOutputDTO updateFuentes(String identificador, List<String> fuentes);
    public void consensuarColecciones();
}
