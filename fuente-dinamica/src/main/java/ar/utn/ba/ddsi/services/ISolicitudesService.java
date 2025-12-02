package ar.utn.ba.ddsi.services;

import ar.utn.ba.ddsi.models.dto.input.HechoInputDTO;
import ar.utn.ba.ddsi.models.dto.input.ResolucionDTO;
import ar.utn.ba.ddsi.models.dto.output.SolicitudCreadaDTO;
import ar.utn.ba.ddsi.models.dto.output.SolicitudDeModificacionOutputDTO;
import ar.utn.ba.ddsi.models.dto.output.SolicitudResueltaDTO;

import java.util.List;

public interface ISolicitudesService {
  SolicitudResueltaDTO procesarSoliPendiente(Long id, ResolucionDTO resolucion, Long adminId);
  SolicitudCreadaDTO crearSolModificacion(Long id, HechoInputDTO hechoNuevo, Long contribuyenteId);
  List<Long> obtenerIDsModificacionPendientes();
  List<SolicitudDeModificacionOutputDTO> obtenerSolicitudesDeModificacion();
  List<SolicitudDeModificacionOutputDTO> obtenerSolicitudesDeModificacionPendientes();
  SolicitudDeModificacionOutputDTO obtenerSolicitudDeModificacionPorID(Long id);
}
