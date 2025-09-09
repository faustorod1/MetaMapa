package ar.utn.ba.ddsi.models.dtos.inputs;

import lombok.Data;

import java.util.List;

@Data
public class CategoriaInputDTO {
  private Long id;
  private String nombre;
  private List<String> sinonimos;
}
