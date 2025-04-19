package ar.edu.utn.frba.dds;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.Scanner;

@Getter
@Setter
public class SolicitudDeEliminacion {
    private String descripcion;
    private Hecho hecho;
    private LocalDateTime fechaDeCarga;
    private LocalDateTime fechaDeResolucion;
    private boolean estaPendiente;

    public SolicitudDeEliminacion(Hecho hecho, String descripcion) throws Exception {
        if(descripcion.length() >= 500) {
            this.descripcion = descripcion;
            this.hecho = hecho;
            this.estaPendiente = true;
            this.fechaDeCarga = LocalDateTime.now();
        }else{
            throw new Exception("La descripcion debe tener al menos de 500 caracteres");
            //TODO: Revisar si es correcto lanzar excepcion
        }
    }

    private void resolver(boolean aceptada){
        if (!estaPendiente) return;

        // TODO: Cómo hacer pasar 1 día antes de resolver?
        this.fechaDeResolucion = LocalDateTime.now();
        estaPendiente = false;
        if (aceptada){
            this.hecho.setEliminado(true);
        }
    }

    public void aceptar() {
        resolver(true);
    }

    public void rechazar() {
        resolver(false);
    }
}