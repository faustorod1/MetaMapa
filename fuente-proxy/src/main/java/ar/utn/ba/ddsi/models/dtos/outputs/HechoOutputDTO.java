package ar.utn.ba.ddsi.models.dtos.outputs;


import ar.utn.ba.ddsi.commons.Coordenada;
import ar.utn.ba.ddsi.models.entities.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;


@AllArgsConstructor
@Builder
@Data
public class HechoOutputDTO {
    private String id;
    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private ContenidoMultimedia contenidoMultimedia;
    private OrigenHecho origen;
    private Coordenada lugarAcontecimiento;
    private LocalDate fechaHecho;
    private LocalDateTime fechaDeCarga;
    private LocalDateTime fechaUltimaActualizacion;
    private boolean eliminado;
    private Long contribuyenteId;
    private List<SolicitudDeEliminacion> solicitudesDeEliminacion;
    private HashSet<Etiqueta> etiquetas;
}