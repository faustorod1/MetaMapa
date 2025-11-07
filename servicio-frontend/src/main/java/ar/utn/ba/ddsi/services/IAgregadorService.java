package ar.utn.ba.ddsi.services;

import ar.utn.ba.ddsi.models.dto.input.CategoriaDTO;
import ar.utn.ba.ddsi.models.dto.input.FuenteDTO;
import ar.utn.ba.ddsi.models.dto.input.HechoDTO;
import ar.utn.ba.ddsi.models.dto.output.SolicitudDeEliminacionOutputDTO;

import java.util.List;

public interface IAgregadorService {
  List<HechoDTO> buscarHechos();
  List<FuenteDTO> buscarFuentes();
  HechoDTO pedirHecho(Long id);
  void solicitarEliminacion(SolicitudDeEliminacionOutputDTO solicitud);
  List<HechoDTO> pedirHechosDeContribuyente();
  List<CategoriaDTO> pedirCategorias();
  CategoriaDTO pedirCategoriaPorID(Long id);
}