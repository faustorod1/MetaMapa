package ar.utn.ba.ddsi.models.dtos.input;

import ar.utn.ba.ddsi.models.entities.Hecho;
import ar.utn.ba.ddsi.models.entities.SolicitudDeEliminacion;
import lombok.Data;

@Data
public class SolicitudDeEliminacionInputDTO {
  private String descripcion;
  private Long hechoId;
  private Long solicitanteId;

  public SolicitudDeEliminacion toEntity(Hecho hecho) {
    return SolicitudDeEliminacion.builder()
            .descripcion(this.getDescripcion())
            .hecho(hecho)
            .solicitanteId(this.solicitanteId)
            .build();
  }
}