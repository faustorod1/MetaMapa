package ar.utn.ba.ddsi.models.entities;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SolicitudDeEliminacion {

    private Long id;
    //private String descripcion;
    //private Long hechoId;
    //private LocalDateTime fechaDeCarga;
    private LocalDateTime fechaDeResolucion;
    private EstadoSolicitud estado;
    //private Long solicitante;           // exclusivo para este módulo
    //private Administrador administradorQueResolvio;
}