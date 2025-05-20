package ar.utn.ba.ddsi.services;

import ar.utn.ba.ddsi.models.dtos.output.HechoOutputDTO;

import java.util.List;

public interface IHechosService {
    public List<HechoOutputDTO> buscarTodos();
}
