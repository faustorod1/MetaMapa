package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.models.dto.input.HechoInputDTO;
import ar.utn.ba.ddsi.models.dto.input.ResolucionDTO;
import ar.utn.ba.ddsi.models.dto.output.HechoOutputDTO;
import ar.utn.ba.ddsi.models.entities.EstadoSolicitud;
import ar.utn.ba.ddsi.models.entities.Hecho;
import ar.utn.ba.ddsi.models.entities.HechoSnapshot;
import ar.utn.ba.ddsi.models.entities.SolicitudDeModificacion;
import ar.utn.ba.ddsi.models.repositories.IHechosRepository;
import ar.utn.ba.ddsi.services.IHechosService;
import ar.utn.ba.ddsi.services.ISolicitudesService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static ar.utn.ba.ddsi.models.entities.EstadoSolicitud.ACEPTADA;
import static ar.utn.ba.ddsi.models.entities.EstadoSolicitud.ACEPTADACONSUGERENCIA;

@Service
public class SolicitudesService implements ISolicitudesService {
  private final IHechosService hechosService;

  public SolicitudesService(IHechosService hechosService) {
    this.hechosService = hechosService;
  }


  // --- Métodos expuestos al controller -------------------------------------------------------------------------------


  @Override
  public HechoOutputDTO procesarSoliPendiente(Long id, ResolucionDTO resolucion){
    Hecho h = hechosService.getById(id);
    h.getSolicitudDeModificacion().resolver(resolucion);
    EstadoSolicitud estadoNuevo = resolucion.getEstadoNuevo();
    if (estadoNuevo == ACEPTADA || estadoNuevo == ACEPTADACONSUGERENCIA){
      Hecho hechoNuevo = h.getSolicitudDeModificacion().getHechoNuevo();

      HechoSnapshot snapshot = new HechoSnapshot(h);
      h.agregarSnapshot(snapshot);

      hechoNuevo.setLastUpdate(LocalDateTime.now());
      hechosService.update(h, hechoNuevo);
      return hechosService.hechoToDTO(hechoNuevo);
    }
    return hechosService.hechoToDTO(h);
  }

  @Override
  public HechoOutputDTO crearSolModificacion(Long id, HechoInputDTO hecho){
    Hecho h = hechosService.DTOToHecho(hecho);
    Hecho hViejo = this.hechosService.getById(id);
    if(ChronoUnit.DAYS.between(h.getFechaDeCarga(), LocalDateTime.now()) <= 7) { // Pasaron menos de 7 días
      if (hViejo.getContribuyente().getId().equals(h.getContribuyente().getId())) { // El que intenta modificar el hecho es quien lo subió
        SolicitudDeModificacion nuevaSolicitudDeModificacion = new SolicitudDeModificacion(hViejo,h);
        hViejo.setSolicitudDeModificacion(nuevaSolicitudDeModificacion);
        return hechosService.hechoToDTO(h);
      }
    }
    return null;
  }
}
