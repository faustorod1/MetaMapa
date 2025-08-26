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

    public SolicitudDeEliminacion(SolicitudDeEliminacionBuilder builder){
        descripcion = builder.getDescripcion();
        hecho = builder.getHecho();
        solicitante = builder.getSolicitante();
        fechaDeCarga = builder.getFechaDeCarga();
        estado = builder.getEstado();
    }

    public static SolicitudDeEliminacionBuilder builder() {
        return new SolicitudDeEliminacionBuilder();
    }

    public void resolver(EstadoSolicitud estado, Administrador administrador) {
        if (this.estado != EstadoSolicitud.PENDIENTE) {return;}

        this.fechaDeResolucion = LocalDateTime.now();
        this.estado = estado;
        this.administradorQueResolvio = administrador;

        if (this.estado == EstadoSolicitud.ACEPTADA) { this.hecho.setEliminado(true); }
    }

}