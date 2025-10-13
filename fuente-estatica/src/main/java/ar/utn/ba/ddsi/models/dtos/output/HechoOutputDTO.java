package ar.utn.ba.ddsi.models.dtos.output;

import ar.utn.ba.ddsi.commons.Coordenada;
import ar.utn.ba.ddsi.models.entities.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Data
public class HechoOutputDTO {
    private Long id;
    private String tipoDeFuente;
    private Long subFuenteId;
    private String titulo;
    private String descripcion;
    private String categoria;
    private List<String> contenidosMultimedia;
    private OrigenHecho origen;
    private Coordenada lugarAcontecimiento;
    private LocalDateTime fechaHecho;
    private LocalDateTime fechaDeCarga;
    private LocalDateTime fechaUltimaActualizacion;
    private boolean eliminado;
    private Long contribuyenteId;
    private HashSet<Etiqueta> etiquetas;
}
