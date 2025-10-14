package ar.utn.ba.ddsi.models.entities;

import ar.utn.ba.ddsi.commons.DetectorDeSpam;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SolicitudDeEliminacionBuilder {
    private final int CANT_MINIMA_DE_CARACTERES = 500;
    private String descripcion;
    private Hecho hecho;
    private LocalDateTime fechaDeCarga;
    private Long solicitanteId;
    private EstadoSolicitud estado;

    public SolicitudDeEliminacionBuilder descripcion(String d) {
        this.descripcion = d;
        return this;
    }
    public SolicitudDeEliminacionBuilder hecho(Hecho h) {
        this.hecho = h;
        return this;
    }
    public SolicitudDeEliminacionBuilder solicitanteId(Long s) {
        this.solicitanteId = s;
        return this;
    }

    public SolicitudDeEliminacion build(){
        if(descripcion.length() < CANT_MINIMA_DE_CARACTERES) {
            this.estado = EstadoSolicitud.RECHAZADA_POR_FALTA_DE_CARACTERES;
        } else if (DetectorDeSpam.esSpam(descripcion)) {
            this.estado = EstadoSolicitud.RECHAZADA_POR_SPAM;
        } else {
            this.estado = EstadoSolicitud.PENDIENTE;
        }
        this.fechaDeCarga = LocalDateTime.now();
        return new SolicitudDeEliminacion(this);
    }
}