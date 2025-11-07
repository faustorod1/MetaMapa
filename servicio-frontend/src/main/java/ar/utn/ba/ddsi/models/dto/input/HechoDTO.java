package ar.utn.ba.ddsi.models.dto.input;

import ar.utn.ba.ddsi.models.entities.OrigenHecho;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HechoDTO implements Serializable {
  private Long id;
  private String titulo;
  private String descripcion;
  private CategoriaDTO categoria;
  private List<ContenidoMultimediaDTO> contenidosMultimedia;
  private OrigenHecho origen;
  private CoordenadaDTO lugarAcontecimiento;
  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
  private LocalDateTime fechaHecho;
  private LocalDateTime fechaDeCarga;
  private LocalDateTime fechaUltimaActualizacion;
  private IdExternoDTO idExterno;
  private Long contribuyente;
  private HashSet<String> etiquetas;
  private DepartamentoDTO departamento;
}