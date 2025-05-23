package ar.utn.ba.ddsi.services;

import ar.utn.ba.ddsi.models.dtos.externals.HechoDTO;
import ar.utn.ba.ddsi.models.dtos.outputs.HechoOutputDTO;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ifuenteProxyServices {
  Mono<List<HechoDTO>> getAll();
  Mono<HechoDTO> getById(Long id);
  Mono<List<HechoDTO>> consumirMetamapa(String baseUrl);
  public HechoOutputDTO externalToOutput(HechoDTO hechoDTO);
}
