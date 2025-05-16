package ar.utn.ba.ddsi.models.entities;


import lombok.Getter;
import lombok.Setter;


import static ar.utn.ba.ddsi.models.entities.EstadoSolicitud.*;

@Getter
@Setter
public class SolicitudDeModificacion {
  private Hecho hechoViejo;//este creo q no va
  private Hecho hechoNuevo;
  private EstadoSolicitud estado;
  //TODO: diagramar en ddc

  public SolicitudDeModificacion(Hecho hechoViejo,Hecho hechoNuevo) {
    this.hechoViejo = hechoViejo;
    this.hechoNuevo = hechoNuevo;
    this.estado = PENDIENTE;
  }

  public void resolver(EstadoSolicitud nuevoEstado) {
    if(estado == PENDIENTE){
      this.estado = nuevoEstado;
      this.hechoViejo = hechoNuevo;
    }
  }

}
