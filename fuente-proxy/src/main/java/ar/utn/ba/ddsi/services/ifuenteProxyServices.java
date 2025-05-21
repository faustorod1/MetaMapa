package ar.utn.ba.ddsi.services;

import ar.utn.ba.ddsi.models.dtos.outputs.HechoDTO;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ifuenteProxyServices {
  Mono<List<HechoDTO>> getAll();
  Mono<HechoDTO> getById(Long id);

  //HechoOutputDTO dtoInputToDtoOutput (HechoInputDTO dtoInput);
}
