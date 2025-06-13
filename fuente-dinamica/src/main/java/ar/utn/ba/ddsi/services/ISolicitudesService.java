package ar.utn.ba.ddsi.services;

import ar.utn.ba.ddsi.models.dto.input.HechoInputDTO;
import ar.utn.ba.ddsi.models.dto.input.ResolucionDTO;
import ar.utn.ba.ddsi.models.dto.output.HechoOutputDTO;

public interface ISolicitudesService {
  HechoOutputDTO crearSolModificacion(Long id, HechoInputDTO hechoNuevo);
  HechoOutputDTO procesarSoliPendiente(Long id, ResolucionDTO resolucion);
}
