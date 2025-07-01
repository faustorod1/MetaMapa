package ar.utn.ba.ddsi.services;

import ar.utn.ba.ddsi.models.dtos.outputs.HechoOutputDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface IHechosService {
  List<HechoOutputDTO> getAll();
  List<HechoOutputDTO> getAllAPI();
  List<HechoOutputDTO> getAllAPIDesde(LocalDateTime desde);
  List<HechoOutputDTO> getAllFromMetamapa();
  void marcarComoEliminado(Long id,Long APIid);
  List<HechoOutputDTO> getAllDesde(LocalDateTime desde);
}
