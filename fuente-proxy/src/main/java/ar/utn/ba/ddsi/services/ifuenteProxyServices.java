package ar.utn.ba.ddsi.services;

import ar.utn.ba.ddsi.models.dtos.externals.HechoDTO;
import ar.utn.ba.ddsi.models.dtos.outputs.HechoOutputDTO;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ifuenteProxyServices {
  List<HechoOutputDTO> getAll();
  Mono<List<HechoDTO>> buscarTodos();
  HechoOutputDTO getById(Long id);
  Mono<HechoDTO> buscarPorId(Long id);
  Mono<List<HechoDTO>> consumirMetamapa(String baseUrl);
  public HechoOutputDTO externalToOutput(HechoDTO hechoDTO);
}
