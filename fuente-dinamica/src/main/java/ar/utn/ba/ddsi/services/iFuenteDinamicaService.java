package ar.utn.ba.ddsi.services;

import ar.utn.ba.ddsi.models.dto.input.HechoInputDTO;
import ar.utn.ba.ddsi.models.dto.output.HechoOutputDTO;
import ar.utn.ba.ddsi.models.entities.Contribuyente;
import ar.utn.ba.ddsi.models.entities.EstadoSolicitud;
import ar.utn.ba.ddsi.models.entities.Hecho;

import java.util.List;

public interface iFuenteDinamicaService {
  HechoOutputDTO crearHecho(HechoInputDTO hecho);
  HechoOutputDTO modificarHecho(HechoInputDTO hechoAModificar,HechoInputDTO hechoNuevo);
  List<HechoOutputDTO> obtenerHechosPendientes(Boolean pendiente);
  List<HechoOutputDTO> obtenerHechosDe(Contribuyente contribuyente);
  List<HechoOutputDTO> obtenerTodosHechos();
  void procesarPendiente(Hecho hecho, EstadoSolicitud estadoNuevo);
  Hecho DtoToHecho (HechoInputDTO hechoInputDTO);
  HechoOutputDTO hechoToDTO(Hecho hecho);
}
