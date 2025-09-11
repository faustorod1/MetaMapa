package ar.utn.ba.ddsi.models.dtos.input;

import ar.utn.ba.ddsi.models.dtos.external.ContribuyenteDTO;
import ar.utn.ba.ddsi.models.entities.Administrador;
import ar.utn.ba.ddsi.models.entities.DescripcionSolicitudException;
import ar.utn.ba.ddsi.models.entities.Hecho;
import ar.utn.ba.ddsi.models.entities.SolicitudDeEliminacion;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SolicitudDeEliminacionInputDTO {
  private String descripcion;
  private Long hechoId;
  private ContribuyenteDTO solicitante;
  private Administrador administradorQueResolvio;

  public SolicitudDeEliminacion toEntity(Hecho hecho) {
    return SolicitudDeEliminacion.builder()
            .descripcion(this.getDescripcion())
            .hecho(hecho)
            .solicitante(this.getSolicitante().toEntity())
            .build();
  }
}
