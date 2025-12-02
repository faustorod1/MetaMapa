package ar.utn.ba.ddsi.models.dto.input;

import ar.utn.ba.ddsi.models.entities.EstadoSolicitud;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SolicitudDeModificacionDTO {
    private Long id;
    private Long idHecho;
    private LocalDateTime fechaDeCarga;
    private EstadoSolicitud estado;
    private String motivoDeEstado;
    private Long administradorId;
    private String tituloNuevo;
    private String descripcionNueva;
    private String categoriaNueva;
    private Double latitudNueva;
    private Double longitudNueva;
    private LocalDateTime fechaHechoNueva;
    private Set<String> etiquetasNuevas;
    private List<String> contenidosMultimediaNuevos;


}


