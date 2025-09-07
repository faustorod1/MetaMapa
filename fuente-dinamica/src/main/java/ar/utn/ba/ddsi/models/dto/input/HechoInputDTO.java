package ar.utn.ba.ddsi.models.dto.input;
import ar.utn.ba.ddsi.models.entities.ContenidoMultimedia;
import ar.utn.ba.ddsi.models.entities.Etiqueta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.HashSet;
import java.util.List;

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
  private String fechaHecho;
  private ContribuyenteDTO contribuyente;
  private HashSet<Etiqueta> etiquetas;
}