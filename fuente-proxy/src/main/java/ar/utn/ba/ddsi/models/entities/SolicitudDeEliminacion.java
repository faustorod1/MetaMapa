package ar.utn.ba.ddsi.models.entities;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SolicitudDeEliminacion {

    private String descripcion;
    private Hecho hecho;
    private LocalDateTime fechaDeCarga;
    private LocalDateTime fechaDeResolucion;
    private EstadoSolicitud estado;
    private Contribuyente solicitante;
    private Administrador administradorQueResolvio;
    private final int CANT_MINIMA_DE_CARACTERES = 500;

    public SolicitudDeEliminacion(Hecho hecho, String descripcion, Contribuyente solicitante) throws Exception {
        if(descripcion.length() >= CANT_MINIMA_DE_CARACTERES) {
            this.descripcion = descripcion;
            this.hecho = hecho;
            this.estado = EstadoSolicitud.PENDIENTE;
            this.fechaDeCarga = LocalDateTime.now();
            this.solicitante = solicitante;
        }else{
            throw new Exception("La descripción debe tener al menos de 500 caracteres");
            //TODO: Revisar si es correcto lanzar excepcion
        }
    }


    public void resolver(EstadoSolicitud estado, Administrador administrador) {
        if (this.estado != EstadoSolicitud.PENDIENTE) { return; }

        this.fechaDeResolucion = LocalDateTime.now();
        this.estado = estado;
        this.administradorQueResolvio = administrador;

        if (this.estado == EstadoSolicitud.ACEPTADA) { this.hecho.setEliminado(true); }

    }

}