package ar.utn.ba.ddsi.models.dtos.inputs;

import ar.utn.ba.ddsi.models.entities.EstadoSolicitud;
import ar.utn.ba.ddsi.models.entities.SolicitudDeEliminacion;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SolicitudDeEliminacionInputDTO {
  private Long id;
  private LocalDateTime fechaDeResolucion;
  private EstadoSolicitud estado;

  public SolicitudDeEliminacion toEntity(){
    return SolicitudDeEliminacion.builder()
        .id(this.getId())
        .estado(this.getEstado())
        .fechaDeResolucion(this.getFechaDeResolucion())
        .build();
  }
}
