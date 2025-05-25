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
  private LocalDateTime fechaDeCarga; //TODO revisar si puede modificarse una vez q ya se creo al hecho. Sino usar fechaDeUltimaModificacion
  private boolean eliminado;      // USO: cuando una solDeElim es aceptada, el hecho se mantiene en el sistema pero no se mostrará en ninguna colección.
  private Contribuyente contribuyente;
  private Long id;
  private LocalDateTime lastUpdate;

  @Builder.Default
  private List<SolicitudDeEliminacion> solicitudesDeEliminacion = new ArrayList<>();
  @Builder.Default //si el builder no le da el valor, hace esto por defecto
  private HashSet<Etiqueta> etiquetas = new HashSet<>();
}
