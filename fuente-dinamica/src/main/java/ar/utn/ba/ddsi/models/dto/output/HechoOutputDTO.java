package ar.utn.ba.ddsi.models.dto.output;

import ar.utn.ba.ddsi.models.entities.Categoria;
import ar.utn.ba.ddsi.models.entities.ContenidoMultimedia;
import ar.utn.ba.ddsi.models.entities.Contribuyente;
import ar.utn.ba.ddsi.models.entities.Coordenada;
import ar.utn.ba.ddsi.models.entities.Etiqueta;
import ar.utn.ba.ddsi.models.entities.OrigenHecho;
import ar.utn.ba.ddsi.models.entities.SolicitudDeEliminacion;
import ar.utn.ba.ddsi.models.entities.SolicitudDeModificacion;
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
public class HechoOutputDTO {
  private String titulo;
  private String descripcion;
  private Categoria categoria;
  private ContenidoMultimedia contenidoMultimedia;
  private OrigenHecho origen;
  private Coordenada lugarAcontecimiento;
  private LocalDate fechaHecho;
  private LocalDateTime fechaDeCarga;
  private boolean eliminado;
  private Contribuyente contribuyente;
  private String id;

  @Builder.Default
  private List<SolicitudDeEliminacion> solicitudesDeEliminacion = new ArrayList<>();
  @Builder.Default
  private HashSet<Etiqueta> etiquetas = new HashSet<>();
}
