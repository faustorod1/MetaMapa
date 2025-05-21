package ar.utn.ba.ddsi.models.dtos.outputs;

import ar.utn.ba.ddsi.models.entities.Categoria;
import ar.utn.ba.ddsi.models.entities.Coordenada;
import ar.utn.ba.ddsi.models.entities.OrigenHecho;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Data
public class HechoDTO {
  private String titulo;
  private String descripcion;
  private Categoria categoria;
  private OrigenHecho origen;
  private Coordenada lugarAcontecimiento;
  private LocalDate fechaHecho;
  private LocalDateTime fechaDeCarga;
  private Long id;
}
