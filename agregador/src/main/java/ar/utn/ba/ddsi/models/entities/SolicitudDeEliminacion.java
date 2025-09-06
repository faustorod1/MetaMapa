package ar.utn.ba.ddsi.models.entities;

import ar.utn.ba.ddsi.commons.DetectorDeSpam;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "solicitudes_eliminacion")
public class SolicitudDeEliminacion {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "descripcion")
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "hecho_id", referencedColumnName = "id", nullable = false)
    private Hecho hecho;

    @Column(name = "fecha_carga", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime fechaDeCarga;
    private LocalDateTime fechaDeResolucion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoSolicitud estado;

    @ManyToOne
    @JoinColumn(name = "solicitante_id", referencedColumnName = "id", nullable = false)
    private Contribuyente solicitante;

    @ManyToOne
    @JoinColumn(name = "administrador_que_resolvio_id", referencedColumnName = "id")
    private Administrador administradorQueResolvio;

    private final int CANT_MINIMA_DE_CARACTERES = 500;

    protected SolicitudDeEliminacion() {}

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