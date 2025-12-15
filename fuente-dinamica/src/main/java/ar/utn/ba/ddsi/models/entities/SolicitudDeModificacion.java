package ar.utn.ba.ddsi.models.entities;


import ar.utn.ba.ddsi.commons.Coordenada;
import ar.utn.ba.ddsi.models.dto.input.ResolucionDTO;
import ar.utn.ba.ddsi.models.exceptions.SolicitudYaProcesadaException;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static ar.utn.ba.ddsi.models.entities.EstadoSolicitud.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@Table(name = "solicitudes_de_modificacion")

public class SolicitudDeModificacion {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "hecho_id", referencedColumnName = "id", nullable = false)
  private Hecho hecho;

  // -- Atributos con intento de modificar ---------------
  @Column(name = "tituloNuevo", columnDefinition = "varchar(128)")
  private String tituloNuevo;
  @Column(name = "descripcionNueva", columnDefinition = "TEXT")
  private String descripcionNueva;
  @Column(name = "categoriaNueva", columnDefinition = "varchar(128)")
  private String categoriaNueva;

  @Column(name = "latitudNueva", columnDefinition = "DOUBLE")
  private Double latitudNueva;
  @Column(name = "longitudNueva", columnDefinition = "DOUBLE")
  private Double longitudNueva;

  @Column(name = "fechaHechoNueva", columnDefinition = "DATETIME")
  private LocalDateTime fechaHechoNueva;
  @ElementCollection
  @CollectionTable(name = "etiquetas_nuevas", joinColumns = @JoinColumn(name = "solicitud_de_modificacion_id"))
  @Column(name = "etiqueta")
  private Set<String> etiquetasNuevas;

  @ElementCollection
  @CollectionTable(name = "contenidos_multimedia_nuevo", joinColumns = @JoinColumn(name = "hecho_id"))
  @Column(name = "path")
  private List<String> contenidosMultimediaNuevos;

  //---------------------------------------------

  @Enumerated(EnumType.STRING)
  @Column(name = "estado", nullable = false)
  private EstadoSolicitud estado;

  @Column(name = "motivo_de_estado", columnDefinition = "VARCHAR(80)", nullable = true)
  private String motivoDeEstado;

  @Column(name = "administrador_Id", nullable = true)
  private Long administradorId = null;

  @Column(name = "fecha_de_carga", nullable = false, columnDefinition = "DATETIME")
  private LocalDateTime fechaDeCarga;

  protected SolicitudDeModificacion() {}


  public void resolver(ResolucionDTO resolucion, Long adminId) {
    if (estado != PENDIENTE) {
      throw new SolicitudYaProcesadaException(this.id);
    }
    
    this.administradorId = adminId;
    this.motivoDeEstado = resolucion.getMotivoDeEstado();
    this.estado = resolucion.getEstadoNuevo();

    if(estado == RECHAZADA){
      hecho.setSolicitudDeModificacion(null);
      return;
    }
    
    if (estado == ACEPTADA || estado == ACEPTADACONSUGERENCIA) {
      HechoSnapshot snapshot = new HechoSnapshot(hecho);
      hecho.agregarSnapshot(snapshot);
      hecho.setFechaUltimaActualizacion(LocalDateTime.now());

      hecho.setEtiquetas(etiquetasNuevas);
      hecho.setFechaHecho(fechaHechoNueva);
      hecho.setLugarAcontecimiento(new Coordenada(latitudNueva, longitudNueva));
      hecho.setDescripcion(descripcionNueva);
      hecho.setCategoria(categoriaNueva);
      hecho.setTitulo(tituloNuevo);
      hecho.setSolicitudDeModificacion(null);

      if(etiquetasNuevas != null){
        hecho.setEtiquetas(etiquetasNuevas);
      }

      if(contenidosMultimediaNuevos != null){
        List<ContenidoMultimedia> nuevasEntidades = contenidosMultimediaNuevos.stream()
            .map(ContenidoMultimedia::new)
            .toList();

        if (hecho.getContenidosMultimedia() == null) {
          hecho.setContenidosMultimedia(new ArrayList<>());
        }

        hecho.getContenidosMultimedia().clear();
        hecho.getContenidosMultimedia().addAll(nuevasEntidades);
      }
    }
  }
}
