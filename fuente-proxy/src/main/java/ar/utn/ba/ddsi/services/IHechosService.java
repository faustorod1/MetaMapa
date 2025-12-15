package ar.utn.ba.ddsi.services;

import ar.utn.ba.ddsi.models.dtos.outputs.HechoOutputDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface IHechosService {
  Page<HechoOutputDTO> getAll(Pageable pageable);
  List<HechoOutputDTO> getAllFromMetamapa();
  Page<HechoOutputDTO> getAllDesde(LocalDateTime desde, Pageable pageable);
  void actualizarHechos();
}
