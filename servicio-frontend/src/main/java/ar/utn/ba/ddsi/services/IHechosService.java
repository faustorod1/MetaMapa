package ar.utn.ba.ddsi.services;

import ar.utn.ba.ddsi.models.dto.input.HechoDTO;
import ar.utn.ba.ddsi.models.dto.output.HechoOutputDTO;

import java.util.List;

public interface IHechosService {
  List<HechoDTO> buscarTodos();
}
