package ar.utn.ba.ddsi.models.dto.output;

import ar.utn.ba.ddsi.models.entities.EstadoSolicitud;
import lombok.Data;

@Data
public class ResolucionSolicitudDeEliminacionOutputDTO {
    private Long administradorQueResolvioId;
    private EstadoSolicitud estadoSolicitud;
}
