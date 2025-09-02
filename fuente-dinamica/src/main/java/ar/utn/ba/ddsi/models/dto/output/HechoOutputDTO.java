package ar.utn.ba.ddsi.models.dto.output;

import ar.utn.ba.ddsi.commons.Coordenada;
import ar.utn.ba.ddsi.models.dto.input.ContribuyenteDTO;
import ar.utn.ba.ddsi.models.entities.Categoria;
import ar.utn.ba.ddsi.models.entities.ContenidoMultimedia;
import ar.utn.ba.ddsi.models.entities.Etiqueta;
import ar.utn.ba.ddsi.models.entities.OrigenHecho;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@AllArgsConstructor
@Builder
@Data
public class HechoOutputDTO {
  private String id;
  private String titulo;
  private String descripcion;
  private Categoria categoria;
  private ContenidoMultimedia contenidoMultimedia;
  private OrigenHecho origen;
  private Coordenada lugarAcontecimiento;
  private LocalDateTime fechaHecho;
  private LocalDateTime fechaDeCarga;
  private LocalDateTime fechaUltimaActualizacion;
  private boolean eliminado;
  private ContribuyenteDTO contribuyente;
  private HashSet<Etiqueta> etiquetas;
}
