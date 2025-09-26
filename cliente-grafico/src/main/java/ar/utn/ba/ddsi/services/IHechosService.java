package ar.utn.ba.ddsi.services;

import ar.utn.ba.ddsi.models.dto.input.HechoDto;

import java.util.List;

public interface IHechosService {
  List<HechoDto> buscarTodos();
}
