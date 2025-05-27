package ar.utn.ba.ddsi.models.dtos.input;

import ar.utn.ba.ddsi.models.dtos.external.ContribuyenteDTO;
import ar.utn.ba.ddsi.models.entities.Administrador;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SolicitudDeEliminacionInputDTO {
  private String descripcion;
  private Long hechoId;
  private ContribuyenteDTO solicitante;
  private Administrador administradorQueResolvio;
}
