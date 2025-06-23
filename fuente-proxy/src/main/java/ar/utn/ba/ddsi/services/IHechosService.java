package ar.utn.ba.ddsi.services;

import ar.utn.ba.ddsi.models.dtos.outputs.HechoOutputDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface IHechosService {
  List<HechoOutputDTO> getAll();
  List<HechoOutputDTO> getAllFromMetamapa();
  List<HechoOutputDTO> getAllDesde(LocalDateTime desde);
  List<HechoOutputDTO> getAllFromAPI(Long APIid);
  List<HechoOutputDTO> getAllDesdeFromAPI(Long APIid,LocalDateTime desde);
  void marcarComoEliminado(Long id,Long APIid);
}
