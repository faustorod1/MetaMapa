package ar.utn.ba.ddsi.models.dtos.externals;

import ar.utn.ba.ddsi.models.entities.OrigenHecho;
import ar.utn.ba.ddsi.models.entities.SolicitudDeEliminacion;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Data
public class HechoExternalMetamapa {
    private Long id;
    private String titulo;
    private String descripcion;
    private String categoria;
    //private String contenidoMultimedia;
    private Double latitud;
    private Double longitud;
    private String fechaHecho;
    private String fechaDeCarga;
    private String fechaUltimaActualizacion;
    //private Long contribuyenteId;
    //private List<SolicitudDeEliminacion> solicitudesDeEliminacion;
    private HashSet<String> etiquetas;
}
