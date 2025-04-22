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

    public SolicitudDeEliminacion(Hecho hecho, String descripcion, LocalDateTime fechaDeCarga) throws Exception {
        if(descripcion.length() >= 500) {
            this.descripcion = descripcion;
            this.hecho = hecho;
            this.estado = Estado.PENDIENTE;
            this.fechaDeCarga = fechaDeCarga;
        }else{
            throw new Exception("La descripcion debe tener al menos de 500 caracteres");
            //TODO: Revisar si es correcto lanzar excepcion
        }
    }


    public void aceptar() {
        if (!estaPendiente()) return;

        this.fechaDeResolucion = LocalDateTime.now();
        this.estado = Estado.ACEPTADA;
        this.hecho.setEliminado(true);
    }

    public void rechazar() {
        if (!estaPendiente()) return;

        this.fechaDeResolucion = LocalDateTime.now();
        estado = Estado.RECHAZADA;
    }

    public boolean estaPendiente() {
        return this.estado == Estado.PENDIENTE;
    }
}