package ar.utn.ba.ddsi.services;

import ar.utn.ba.ddsi.models.dto.input.*;
import ar.utn.ba.ddsi.models.dto.output.ColeccionOutputDTO;
import ar.utn.ba.ddsi.models.dto.output.ResolucionSolicitudDeEliminacionOutputDTO;
import ar.utn.ba.ddsi.models.dto.output.SolicitudDeEliminacionOutputDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IAgregadorService {
  List<HechoDTO> buscarHechos();
  Page<HechoDTO> buscarHechos(int page, int size);
  List<FuenteDTO> buscarFuentes();
  HechoDTO pedirHecho(Long id);
  void solicitarEliminacion(SolicitudDeEliminacionOutputDTO solicitud);
  List<HechoDTO> pedirHechosDeContribuyente();
  List<CategoriaDTO> pedirCategorias();
  CategoriaDTO pedirCategoriaPorID(Long id);
  List<Long> pedirIDsEliminacionesPendientes();
  SolicitudDeEliminacionDTO pedirSolicitudDeEliminacion(Long id);
  void resolverEliminacion(Long id, ResolucionSolicitudDeEliminacionOutputDTO solicitud);
  List<Long> pedirIDsExternosDinamica();
  HechoDTO pedirHechoDinamica(Long id_externo);
  List<String> pedirIdentificadoresDeColecciones();
  void cargarColeccion(ColeccionOutputDTO coleccion);
  List<ColeccionConHechosDTO> pedirColeccionesConHechos();
  List<ColeccionConHechosDTO> pedirColeccionesConHechosCurados();
  List<ColeccionDTO> pedirColecciones();

}