package ar.utn.ba.ddsi.models.dto.output;

import ar.utn.ba.ddsi.models.entities.EstadoSolicitud;

public class SolicitudResueltaDTO {
    Long id;
    EstadoSolicitud estadoSolicitud;
    HechoOutputDTO hecho;

    public SolicitudResueltaDTO(Long id, EstadoSolicitud estadoSolicitud, HechoOutputDTO hecho) {
        this.id = id;
        this.estadoSolicitud = estadoSolicitud;
        this.hecho = hecho;
    }
}
