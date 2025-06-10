package ar.utn.ba.ddsi.models.entities;

import ar.utn.ba.ddsi.commons.DetectorDeSpam;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SolicitudDeEliminacion {

    private Long id;
    private String descripcion;
    private Hecho hecho;
    private LocalDateTime fechaDeCarga;
    private LocalDateTime fechaDeResolucion;
    private EstadoSolicitud estado;
    private Contribuyente solicitante;
    private Administrador administradorQueResolvio;
    private final int CANT_MINIMA_DE_CARACTERES = 500;

    public SolicitudDeEliminacion(Hecho hecho, String descripcion, Contribuyente solicitante){
        this.descripcion = descripcion;
        this.hecho = hecho;
        this.fechaDeCarga = LocalDateTime.now();
        this.solicitante = solicitante;
        if(descripcion.length() < CANT_MINIMA_DE_CARACTERES) {
            this.estado = EstadoSolicitud.RECHAZADA_POR_FALTA_DE_CARACTERES;
        }
        if (DetectorDeSpam.esSpam(descripcion)) {
            this.estado = EstadoSolicitud.RECHAZADA_POR_SPAM;
        }
        this.estado = EstadoSolicitud.PENDIENTE;
    }


    public void resolver(EstadoSolicitud estado, Administrador administrador) {
        if (this.estado != EstadoSolicitud.PENDIENTE) { return; }

        this.fechaDeResolucion = LocalDateTime.now();
        this.estado = estado;
        this.administradorQueResolvio = administrador;

        if (this.estado == EstadoSolicitud.ACEPTADA) { this.hecho.setEliminado(true); }
    }

}