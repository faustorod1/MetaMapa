package ar.utn.ba.ddsi.models.dto.input;
import ar.utn.ba.ddsi.models.entities.ContenidoMultimedia;
import ar.utn.ba.ddsi.models.entities.Etiqueta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Builder
@Data

public class HechoInputDTO {
  private String titulo;
  private String descripcion;
  private String categoria;
  private List<String> contenidosMultimedia;
  private Double latitud;
  private Double longitud;
  private LocalDate fechaHecho;
  private Long contribuyenteId;
  private List <EtiquetaDTO> etiquetas;
}