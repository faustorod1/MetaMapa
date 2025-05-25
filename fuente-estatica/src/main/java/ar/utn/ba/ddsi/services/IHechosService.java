package ar.utn.ba.ddsi.services;

import ar.utn.ba.ddsi.models.dtos.output.HechoOutputDTO;
import ar.utn.ba.ddsi.models.entities.Hecho;

import java.util.List;

public interface IHechosService {
    public List<HechoOutputDTO> buscarTodos();
    public HechoOutputDTO findById(Long id);
    public List<Hecho> guardarHechos();
}
