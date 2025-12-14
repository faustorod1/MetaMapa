package ar.utn.ba.ddsi.services;

import ar.utn.ba.ddsi.models.dtos.input.ResolucionSolicitudDeEliminacionDTO;
import ar.utn.ba.ddsi.models.dtos.input.SolicitudDeEliminacionInputDTO;
import ar.utn.ba.ddsi.models.dtos.output.SolicitudDeEliminacionOutputDTO;
import ar.utn.ba.ddsi.models.entities.SolicitudDeEliminacion;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ISolicitudesService {
    SolicitudDeEliminacion crearSolicitud(SolicitudDeEliminacionInputDTO solicitud);
    SolicitudDeEliminacion modificarEstadoSolicitud(Long id, ResolucionSolicitudDeEliminacionDTO resolucionDto);
    List<SolicitudDeEliminacionOutputDTO> obtenerSolicitudesDeEliminacion();
    List<Long> obtenerIDsEliminacionPendientes();
    SolicitudDeEliminacionOutputDTO obtenerSolicitudDeEliminacionPorID(Long id);
    List<SolicitudDeEliminacionOutputDTO> obtenerSolicitudesDeEliminacionPendientes();
    Long obtenerCantidadAceptadas();
    Long obtenerCantidadRechazadas();
}
