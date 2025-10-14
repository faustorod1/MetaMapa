package ar.utn.ba.ddsi.models.entities;


import ar.utn.ba.ddsi.models.dto.input.ResolucionDTO;
import jakarta.persistence.*;
import lombok.Data;


import static ar.utn.ba.ddsi.models.entities.EstadoSolicitud.*;

@Data
@Entity
@Table(name = "solicitudes_de_modificacion")
public class SolicitudDeModificacion {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "hecho_viejo_id", referencedColumnName = "id", nullable = false)
  private Hecho hechoViejo;

  @ManyToOne
  @JoinColumn(name = "hecho_nuevo_id", referencedColumnName = "id", nullable = false)
  private Hecho hechoNuevo;

  @Enumerated(EnumType.STRING)
  @Column(name = "estado", nullable = false)
  private EstadoSolicitud estado;

  @Column(name = "motivo_de_estado", columnDefinition = "VARCHAR(80)", nullable = false)
  private String motivoDeEstado;

  @Column(name = "administrador_Id", nullable = true)
  private Long administradorId = null;

  protected SolicitudDeModificacion() {}

  public SolicitudDeModificacion(Hecho hechoViejo, Hecho hechoNuevo) {
    this.hechoViejo = hechoViejo;
    this.hechoNuevo = hechoNuevo;
    this.estado = PENDIENTE;
  }

  public void resolver(ResolucionDTO resolucion) {
    if(estado == PENDIENTE){
      this.administradorId = resolucion.getAdministradorId();
      this.motivoDeEstado = resolucion.getMotivoDeEstado();
      this.estado = resolucion.getEstadoNuevo();
    }
  }

}
