package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.models.dto.input.HechoInputDTO;
import ar.utn.ba.ddsi.models.dto.input.ResolucionDTO;
import ar.utn.ba.ddsi.models.dto.output.SolicitudCreadaDTO;
import ar.utn.ba.ddsi.models.dto.output.SolicitudResueltaDTO;
import ar.utn.ba.ddsi.models.entities.Hecho;
import ar.utn.ba.ddsi.models.entities.SolicitudDeModificacion;
import ar.utn.ba.ddsi.models.exceptions.NoHaySolicitudPendienteException;
import ar.utn.ba.ddsi.models.exceptions.SolicitudFueraDePlazoException;
import ar.utn.ba.ddsi.models.exceptions.UnauthorizedException;
import ar.utn.ba.ddsi.services.IHechosService;
import ar.utn.ba.ddsi.services.ISolicitudesService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class SolicitudesService implements ISolicitudesService {
  private final IHechosService hechosService;

  public SolicitudesService(IHechosService hechosService) {
    this.hechosService = hechosService;
  }


  // --- Métodos expuestos al controller -------------------------------------------------------------------------------


  @Override
  public SolicitudResueltaDTO procesarSoliPendiente(Long id, ResolucionDTO resolucion, Long adminId) {

    Hecho hechoViejo = hechosService.getById(id);
    if (hechoViejo == null) {
      throw new EntityNotFoundException("Hecho no encontrado");
    }

    if (hechoViejo.getSolicitudDeModificacion() == null) {
      throw new NoHaySolicitudPendienteException(id);
    }

    SolicitudDeModificacion solicitud = hechoViejo.getSolicitudDeModificacion();
    solicitud.resolver(resolucion, adminId);

    hechosService.guardarCambios(hechoViejo);

    return new SolicitudResueltaDTO(solicitud.getId(), solicitud.getEstado(), hechosService.hechoToDTO(hechoViejo));
  }

  @Override
  public SolicitudCreadaDTO crearSolModificacion(Long id, HechoInputDTO hecho, Long contribuyenteId){
    Hecho h = hechosService.DTOToHecho(hecho);
    Hecho hViejo = this.hechosService.getById(id);

    if (!hViejo.getContribuyenteId().equals(contribuyenteId)) {
        throw new UnauthorizedException(contribuyenteId);
    }
    if(ChronoUnit.DAYS.between(hViejo.getFechaDeCarga(), LocalDateTime.now()) > 7) {
        throw new SolicitudFueraDePlazoException(id);
    }

    SolicitudDeModificacion nuevaSolicitudDeModificacion = new SolicitudDeModificacion(hViejo,h);
    hViejo.setSolicitudDeModificacion(nuevaSolicitudDeModificacion);

    hechosService.guardarCambios(hViejo);

    return new SolicitudCreadaDTO(hViejo.getSolicitudDeModificacion().getId(), hViejo.getId());
  }
}
