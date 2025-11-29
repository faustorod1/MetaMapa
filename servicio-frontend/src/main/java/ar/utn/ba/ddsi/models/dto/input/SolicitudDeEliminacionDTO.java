package ar.utn.ba.ddsi.models.dto.input;

import ar.utn.ba.ddsi.models.entities.EstadoSolicitud;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SolicitudDeEliminacionDTO implements Serializable {
  Long id;
  String descripcion;
  Long hechoID;
  LocalDateTime fechaDeCarga;
  LocalDateTime fechaDeResolucion;
  EstadoSolicitud estado;
  Long solicitanteID;
  Long administradorQueResolvio;
}





