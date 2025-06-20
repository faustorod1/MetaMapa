package ar.utn.ba.ddsi.services;

import ar.utn.ba.ddsi.models.dtos.externals.HechoExternalDTO;
import ar.utn.ba.ddsi.models.dtos.outputs.HechoOutputDTO;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

public interface IHechosServices {
  List<HechoOutputDTO> getAllDesde(LocalDateTime desde);
  List<HechoOutputDTO> getAll();
  Mono<List<HechoExternalDTO>> buscarTodos();
  HechoOutputDTO getById(Long id);
  Mono<HechoExternalDTO> buscarPorId(Long id);
  List<HechoOutputDTO> consumirMetamapa(String baseUrl);
}
