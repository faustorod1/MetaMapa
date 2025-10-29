package ar.utn.ba.ddsi.services;

import ar.utn.ba.ddsi.models.dto.input.HechoInputDTO;
import ar.utn.ba.ddsi.models.dto.input.ResolucionDTO;
import ar.utn.ba.ddsi.models.dto.output.SolicitudCreadaDTO;
import ar.utn.ba.ddsi.models.dto.output.SolicitudResueltaDTO;

public interface ISolicitudesService {
  SolicitudResueltaDTO procesarSoliPendiente(Long id, ResolucionDTO resolucion, Long adminId);
  SolicitudCreadaDTO crearSolModificacion(Long id, HechoInputDTO hechoNuevo, Long contribuyenteId);
}
