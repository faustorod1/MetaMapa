package ar.utn.ba.ddsi.models.dtos.inputs;

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
  private Long solicitante;
  private Administrador administradorQueResolvio;
}
