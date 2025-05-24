package ar.utn.ba.ddsi.services;

import ar.utn.ba.ddsi.models.dtos.output.ColeccionOutputDTO;
import ar.utn.ba.ddsi.models.dtos.output.HechoOutputDTO;

import java.util.List;

public interface IColeccionesService {

    List<ColeccionOutputDTO> buscarTodos();

    List<HechoOutputDTO> buscarHechosPorColeccion(String identificador);
}
