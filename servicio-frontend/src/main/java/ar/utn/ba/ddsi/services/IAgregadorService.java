package ar.utn.ba.ddsi.services;

import ar.utn.ba.ddsi.models.dto.input.*;
import ar.utn.ba.ddsi.models.dto.output.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IAgregadorService {
  List<HechoDTO> buscarHechos();
  Page<HechoDTO> buscarHechos(int page, int size);
  List<FuenteDTO> buscarFuentes();
  HechoDTO pedirHecho(Long id);
  SolicitudDeEliminacionDTO solicitarEliminacion(SolicitudDeEliminacionOutputDTO solicitud);
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
  ColeccionDTO pedirColeccionPorId(String identificador);
  void eliminarColeccion(String identificador);
  void actualizarColeccion(ColeccionOutputDTO coleccion);
  ColeccionConHechosDTO pedirColeccionConHechos(String id, int pagina, int tamanio, String modo);
  Page<HechoDTO> pedirHechosDeContribuyentePaginado(int page, int size);
  Long pedirSolicitudesAceptadas();
  Long pedirSolicitudesRechazadas();
  HechoDTO pedirUltimoHechoCargado();
  Integer pedirCantidadDeHechosEnElSistema();
  void actualizarHechos();
  void consensuarColecciones();
  ColeccionConHechosDTO filtrarHechosColeccion(String identificador, List<FiltroOutputDTO> filtros, int pagina, int tamanio);
}