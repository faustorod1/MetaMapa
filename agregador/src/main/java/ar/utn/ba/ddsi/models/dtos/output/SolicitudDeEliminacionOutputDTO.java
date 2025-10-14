package ar.utn.ba.ddsi.models.dtos.output;

import ar.utn.ba.ddsi.models.entities.EstadoSolicitud;
import ar.utn.ba.ddsi.models.entities.SolicitudDeEliminacion;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SolicitudDeEliminacionOutputDTO {
  private Long id;
  private String descripcion;
  private Long hechoId;
  private LocalDateTime fechaDeCarga;
  private LocalDateTime fechaDeResolucion;
  private EstadoSolicitud estado;
  private Long solicitanteId;
  private Long administradorQueResolvioId;

  public static SolicitudDeEliminacionOutputDTO fromEntity(SolicitudDeEliminacion solicitud) {
    SolicitudDeEliminacionOutputDTO dto = new SolicitudDeEliminacionOutputDTO();
    dto.setId(solicitud.getId());
    dto.setDescripcion(solicitud.getDescripcion());
    dto.setHechoId(solicitud.getHecho().getId());
    dto.setFechaDeCarga(solicitud.getFechaDeCarga());
    dto.setFechaDeResolucion(solicitud.getFechaDeResolucion());
    dto.setEstado(solicitud.getEstado());
    dto.setSolicitanteId(solicitud.getSolicitanteId());
    dto.setAdministradorQueResolvioId(solicitud.getAdministradorQueResolvioId());
    return dto;
  }
}
