package ar.utn.ba.ddsi.models.dtos.external;

import ar.utn.ba.ddsi.commons.Coordenada;
import ar.utn.ba.ddsi.models.entities.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Data
public class HechoFuenteDTO {
    private String id;
    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private ContenidoMultimedia contenidoMultimedia;
    private OrigenHecho origen;
    private Coordenada lugarAcontecimiento;
    private LocalDateTime fechaHecho;
    private LocalDateTime fechaDeCarga;
    private boolean eliminado;
    private ContribuyenteDTO contribuyente;
    private List<SolicitudDeEliminacion> solicitudesDeEliminacion;
    private HashSet<Etiqueta> etiquetas;
}
