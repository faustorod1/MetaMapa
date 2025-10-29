package ar.utn.ba.ddsi.models.dto.output;

import ar.utn.ba.ddsi.models.entities.EstadoSolicitud;

public class SolicitudCreadaDTO {
    Long id;
    Long hechoId;
    EstadoSolicitud estadoSolicitud = EstadoSolicitud.PENDIENTE;

    public SolicitudCreadaDTO(Long id, Long hechoId) {
        this.id = id;
        this.hechoId = hechoId;
    }
}
