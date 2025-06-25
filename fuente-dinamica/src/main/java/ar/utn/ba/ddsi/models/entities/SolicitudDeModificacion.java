package ar.utn.ba.ddsi.models.entities;


import ar.utn.ba.ddsi.models.dto.input.ResolucionDTO;
import lombok.Data;


import static ar.utn.ba.ddsi.models.entities.EstadoSolicitud.*;

@Data
public class SolicitudDeModificacion {
  private Hecho hechoViejo;//este creo q no va
  private Hecho hechoNuevo;
  private EstadoSolicitud estado;
  private String motivoDeEstado;

  private Administrador administrador = null;

  public SolicitudDeModificacion(Hecho hechoViejo, Hecho hechoNuevo) {
    this.hechoViejo = hechoViejo;
    this.hechoNuevo = hechoNuevo;
    this.estado = PENDIENTE;
  }

  public void resolver(ResolucionDTO resolucion) {
    if(estado == PENDIENTE){
      this.administrador = resolucion.getAdministrador();
      this.motivoDeEstado = resolucion.getMotivoDeEstado();
      this.estado = resolucion.getEstadoNuevo();
    }
  }

}
