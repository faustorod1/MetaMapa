package ar.utn.ba.ddsi.models.dto.input;

import ar.utn.ba.ddsi.models.entities.Administrador;
import ar.utn.ba.ddsi.models.entities.EstadoSolicitud;
import lombok.Data;

@Data
public class ResolucionDTO {
  private Administrador administrador;
  private String motivoDeEstado;
  private EstadoSolicitud estadoNuevo;
}
