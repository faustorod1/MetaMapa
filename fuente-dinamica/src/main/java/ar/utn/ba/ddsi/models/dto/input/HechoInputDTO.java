package ar.utn.ba.ddsi.models.dto.input;
import ar.utn.ba.ddsi.models.entities.Categoria;
import ar.utn.ba.ddsi.models.entities.ContenidoMultimedia;
import ar.utn.ba.ddsi.models.entities.Contribuyente;
import ar.utn.ba.ddsi.models.entities.Coordenada;
import ar.utn.ba.ddsi.models.entities.Etiqueta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@AllArgsConstructor
@Builder
@Data
public class HechoInputDTO {
  private String titulo;
  private String descripcion;
  private Categoria categoria;
  private ContenidoMultimedia contenidoMultimedia;
  private Coordenada lugarAcontecimiento;
  private LocalDate fechaHecho;
  private Contribuyente contribuyente;

  @Builder.Default
  private HashSet<Etiqueta> etiquetas = new HashSet<>();
}