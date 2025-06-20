package ar.utn.ba.ddsi.models.dtos.externals;

import lombok.*;

@AllArgsConstructor
@Builder
@Data
public class HechoExternalDTO {
  private Long id;
  private String titulo;
  private String descripcion;
  private String categoria;
  private Double latitud;
  private Double longitud;
  private String fecha_hecho;
  private String created_at;
  private String updated_at;
}



