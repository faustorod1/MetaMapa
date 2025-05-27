package ar.utn.ba.ddsi.services;

import ar.utn.ba.ddsi.models.dto.input.HechoInputDTO;
import ar.utn.ba.ddsi.models.dto.output.HechoOutputDTO;
import ar.utn.ba.ddsi.models.entities.Contribuyente;
import ar.utn.ba.ddsi.models.entities.EstadoSolicitud;
import ar.utn.ba.ddsi.models.entities.Hecho;

import java.time.LocalDateTime;
import java.util.List;

public interface iFuenteDinamicaService {
  HechoOutputDTO crearHecho(HechoInputDTO hecho);
  HechoOutputDTO modificarHecho(Long id,HechoInputDTO hechoNuevo);
  List<HechoOutputDTO> obtenerHechosPendientes(Boolean pendiente);
  List<HechoOutputDTO> obtenerHechosDe(Contribuyente contribuyente);
  List<HechoOutputDTO> getAll();
  List<HechoOutputDTO> getAllDesde(LocalDateTime desde);
  HechoOutputDTO procesarPendiente(Long id, EstadoSolicitud estadoNuevo);
  Hecho DtoToHecho (HechoInputDTO hechoInputDTO);
  HechoOutputDTO hechoToDTO(Hecho hecho);
}
