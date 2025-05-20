package ar.utn.ba.ddsi.models.dtos.inputs;

import lombok.Data;

@Data
public class HechoInputDTO {
  private Long id;
  private String titulo;
  private String descripcion;
  private String categoria;
  private Double latitud;
  private Double longitud;
  private String fechaHecho;
  private String createdAT;
  private String updatedAT;
}
