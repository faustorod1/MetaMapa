package ar.utn.ba.ddsi.models.dto.input;

import ar.utn.ba.ddsi.models.entities.EstadoSolicitud;
import lombok.Data;

import java.util.List;

@Data
public class ResolucionDTO {
  private String motivoDeEstado;
  private EstadoSolicitud estadoNuevo;
  private List<String> imagenesConfirmadas;
}
