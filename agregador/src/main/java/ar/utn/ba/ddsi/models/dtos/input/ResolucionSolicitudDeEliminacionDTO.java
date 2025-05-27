package ar.utn.ba.ddsi.models.dtos.input;

import ar.utn.ba.ddsi.models.entities.Administrador;
import ar.utn.ba.ddsi.models.entities.EstadoSolicitud;
import lombok.Data;

@Data
public class ResolucionSolicitudDeEliminacionDTO {
  private Administrador administradorQueResolvio;
  private EstadoSolicitud estadoSolicitud;
}
