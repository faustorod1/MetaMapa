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
@Table(name = "solicitudes_de_eliminacion")
public class SolicitudDeEliminacion {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "descripcion", columnDefinition = "TEXT",  nullable = false)
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "hecho_id", referencedColumnName = "id", nullable = false)
    private Hecho hecho;

    @Column(name = "fecha_carga", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime fechaDeCarga;

    @Column(name = "fecha_resolucion", columnDefinition = "DATETIME")
    private LocalDateTime fechaDeResolucion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", columnDefinition = "varchar(50)", nullable = false)
    private EstadoSolicitud estado;

    @Column(name = "solicitante_id", nullable = false)
    private Long solicitanteId;

    @Column(name = "administrador_que_resolvio_id")
    private Long administradorQueResolvioId;

    private final int CANT_MINIMA_DE_CARACTERES = 500;

    protected SolicitudDeEliminacion() {}

    public SolicitudDeEliminacion(SolicitudDeEliminacionBuilder builder){
        descripcion = builder.getDescripcion();
        hecho = builder.getHecho();
        solicitanteId = builder.getSolicitanteId();
        fechaDeCarga = builder.getFechaDeCarga();
        estado = builder.getEstado();
    }

    public static SolicitudDeEliminacionBuilder builder() {
        return new SolicitudDeEliminacionBuilder();
    }

    public void resolver(EstadoSolicitud estado, Long administrador) {
        if (this.estado != EstadoSolicitud.PENDIENTE) {return;}

        this.fechaDeResolucion = LocalDateTime.now();
        this.estado = estado;
        this.administradorQueResolvioId = administrador;

        if (this.estado == EstadoSolicitud.ACEPTADA) { this.hecho.setEliminado(true); }
    }

}