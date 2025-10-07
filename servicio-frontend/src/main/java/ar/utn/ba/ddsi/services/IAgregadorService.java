package ar.utn.ba.ddsi.services;

import ar.utn.ba.ddsi.models.dto.input.FuenteDTO;
import ar.utn.ba.ddsi.models.dto.input.HechoDTO;

import java.util.List;

public interface IAgregadorService {
  List<HechoDTO> buscarHechos();
  List<FuenteDTO> buscarFuentes();
}
