package ar.utn.ba.ddsi.models.dto.input;

import ar.utn.ba.ddsi.models.entities.EstadoSolicitud;
import lombok.Data;

@Data
public class ResolucionDTO {
  private String motivoDeEstado;
  private EstadoSolicitud estadoNuevo;
}
