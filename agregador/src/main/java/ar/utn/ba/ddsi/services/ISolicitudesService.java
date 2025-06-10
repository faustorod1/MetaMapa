package ar.utn.ba.ddsi.services;

import ar.utn.ba.ddsi.models.dtos.input.ResolucionSolicitudDeEliminacionDTO;
import ar.utn.ba.ddsi.models.dtos.input.SolicitudDeEliminacionInputDTO;
import ar.utn.ba.ddsi.models.dtos.output.SolicitudDeEliminacionOutputDTO;
import ar.utn.ba.ddsi.models.entities.SolicitudDeEliminacion;

public interface ISolicitudesService {
    String crearSolicitud(SolicitudDeEliminacionInputDTO solicitud);
    SolicitudDeEliminacion solicitudDeEliminacionFromDTO(SolicitudDeEliminacionInputDTO dto);
    void modificarEstadoSolicitud(Long id, ResolucionSolicitudDeEliminacionDTO resolucionDto);
    SolicitudDeEliminacionOutputDTO solicititudDeEliminacionToDTO(SolicitudDeEliminacion solicitud);
}
