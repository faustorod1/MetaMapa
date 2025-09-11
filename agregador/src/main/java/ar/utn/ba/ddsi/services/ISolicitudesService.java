package ar.utn.ba.ddsi.services;

import ar.utn.ba.ddsi.models.dtos.input.ResolucionSolicitudDeEliminacionDTO;
import ar.utn.ba.ddsi.models.dtos.input.SolicitudDeEliminacionInputDTO;
import ar.utn.ba.ddsi.models.dtos.output.SolicitudDeEliminacionOutputDTO;
import ar.utn.ba.ddsi.models.entities.SolicitudDeEliminacion;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ISolicitudesService {
    SolicitudDeEliminacion crearSolicitud(SolicitudDeEliminacionInputDTO solicitud);
    void modificarEstadoSolicitud(Long id, ResolucionSolicitudDeEliminacionDTO resolucionDto);
    List<SolicitudDeEliminacionOutputDTO> obtenerSolicitudes();

}
