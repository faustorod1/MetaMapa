package ar.utn.ba.ddsi.models.dto.output;

import ar.utn.ba.ddsi.commons.Coordenada;
import ar.utn.ba.ddsi.models.dto.input.EtiquetaDTO;
import ar.utn.ba.ddsi.models.entities.OrigenHecho;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Builder
@Data
public class HechoOutputDTO {
  private Long id;
  private String tipoDeFuente;
  private String titulo;
  private String descripcion;
  private String categoria;
  private List<String> contenidosMultimedia;
  private OrigenHecho origen;
  private Coordenada lugarAcontecimiento;
  private LocalDateTime fechaHecho;
  private LocalDateTime fechaDeCarga;
  private LocalDateTime fechaUltimaActualizacion;
  private boolean eliminado;
  private Long contribuyenteId;
  private Set<EtiquetaDTO> etiquetas;
}
