package ar.utn.ba.ddsi.models.dto.input;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Builder
@Data

public class HechoInputDTO {
  private String titulo;
  private String descripcion;
  private String categoria;
  //private String contenidoMultimedia;
  private Double latitud;
  private Double longitud;
  private String fechaHecho;
  private ContribuyenteDTO contribuyente;
  private List<String> etiquetas;
}