package ar.utn.ba.ddsi.services;

import ar.utn.ba.ddsi.models.dtos.output.ColeccionOutputDTO;
import ar.utn.ba.ddsi.models.dtos.output.HechoOutputDTO;
import ar.utn.ba.ddsi.models.entities.Criterio;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IColeccionesService {
    List<ColeccionOutputDTO> buscarTodos();
    Mono<List<HechoOutputDTO>> buscarHechosPorColeccion(String identificador);
    void crearColeccion(String identificador, String titulo, String descripcion, Criterio criterioDePertenencia, List<String> fuentes);
}
