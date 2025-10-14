package ar.utn.ba.ddsi.models.dtos.input;

import ar.utn.ba.ddsi.models.entities.EstadoSolicitud;
import lombok.Data;

@Data
public class ResolucionSolicitudDeEliminacionDTO {
  private Long administradorQueResolvioId;
  private EstadoSolicitud estadoSolicitud;
}
