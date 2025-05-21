package ar.utn.ba.ddsi.models.dtos.external;

import ar.utn.ba.ddsi.commons.Coordenada;
import ar.utn.ba.ddsi.models.entities.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Data
public class FuenteHechoDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private String categoria;
    private String contenidoMultimedia;
    private int origen;
    private double[] lugarAcontecimiento;
    private LocalDate fechaHecho;
    private LocalDateTime fechaDeCarga;
    private Long contribuyente;
    private List<SolicitudDeEliminacion> solicitudesDeEliminacion; //
    private HashSet<String> etiquetas;
}
