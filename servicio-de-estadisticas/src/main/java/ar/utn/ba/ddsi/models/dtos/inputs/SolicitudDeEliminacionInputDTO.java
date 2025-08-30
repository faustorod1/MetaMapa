package ar.utn.ba.ddsi.models.dtos.input;

import ar.utn.ba.ddsi.models.entities.Administrador;
import ar.utn.ba.ddsi.models.entities.EstadoSolicitud;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SolicitudDeEliminacionInputDTO {
  private Long id;
  private String descripcion;
  private Long hechoId;
  private LocalDateTime fechaDeCarga;
  private LocalDateTime fechaDeResolucion;
  private EstadoSolicitud estado;
  private ContribuyenteDTO solicitante;
  private Administrador administradorQueResolvio;
}
