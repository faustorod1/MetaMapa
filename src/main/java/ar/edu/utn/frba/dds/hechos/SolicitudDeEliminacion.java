package ar.edu.utn.frba.dds;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class SolicitudDeEliminacion {
    enum Estado {
        PENDIENTE,
        ACEPTADA,
        RECHAZADA
    };

    private String descripcion;
    private Hecho hecho;
    private LocalDateTime fechaDeCarga;
    private LocalDateTime fechaDeResolucion;
    private Estado estado;
    private Contribuyente solicitante;
    private Administrador administradorQueResolvio;

    public SolicitudDeEliminacion(Hecho hecho, String descripcion, Contribuyente solicitante) throws Exception {
        if(descripcion.length() >= 500) {
            this.descripcion = descripcion;
            this.hecho = hecho;
            this.estado = Estado.PENDIENTE;
            this.fechaDeCarga = LocalDateTime.now();
            this.solicitante = solicitante;
        }else{
            throw new Exception("La descripción debe tener al menos de 500 caracteres");
            //TODO: Revisar si es correcto lanzar excepcion
        }
    }


    public void resolver(Estado estado, Administrador administrador) {
        if (this.estado != Estado.PENDIENTE) { return; }

        this.fechaDeResolucion = LocalDateTime.now();
        this.estado = estado;
        this.administradorQueResolvio = administrador;

        if (this.estado == Estado.ACEPTADA) { this.hecho.setEliminado(true); }

    }

    public boolean estaPendiente() {
        return this.estado == Estado.PENDIENTE;
    }
}