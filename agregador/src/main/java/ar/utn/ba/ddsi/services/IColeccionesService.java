package ar.utn.ba.ddsi.services;

import ar.utn.ba.ddsi.models.dtos.output.ColeccionOutputDTO;

import java.util.List;

public interface IColeccionesService {

    public List<ColeccionOutputDTO> buscarTodos();
}
