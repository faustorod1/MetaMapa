package ar.utn.ba.ddsi.services;

import ar.utn.ba.ddsi.models.dtos.inputs.HechoInputDTO;
import ar.utn.ba.ddsi.models.dtos.outputs.HechoOutputDTO;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ifuenteProxyServices {
  Mono<List<HechoOutputDTO>> getAll();


  //HechoOutputDTO dtoInputToDtoOutput (HechoInputDTO dtoInput);
}
