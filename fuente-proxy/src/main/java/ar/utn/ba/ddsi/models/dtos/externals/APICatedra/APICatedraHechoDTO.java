package ar.utn.ba.ddsi.models.dtos.externals.APICatedra;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Data
public class APICatedraHechoDTO {
  private Long id;
  private String titulo;
  private String descripcion;
  private String categoria;
  private Double latitud;
  private Double longitud;
  private LocalDate fecha_hecho;
  private LocalDateTime created_at;
  private LocalDateTime updated_at;
}



