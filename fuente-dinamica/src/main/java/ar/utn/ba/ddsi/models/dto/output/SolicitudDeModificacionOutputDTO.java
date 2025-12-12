package ar.utn.ba.ddsi.models.dto.output;

import ar.utn.ba.ddsi.models.entities.EstadoSolicitud;
import ar.utn.ba.ddsi.models.entities.Hecho;
import ar.utn.ba.ddsi.models.entities.SolicitudDeModificacion;
import jakarta.persistence.*;
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
public class SolicitudDeModificacionOutputDTO {
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



    public static SolicitudDeModificacionOutputDTO fromEntity(SolicitudDeModificacion solicitud) {
        SolicitudDeModificacionOutputDTO dto = new SolicitudDeModificacionOutputDTO();
        dto.setId(solicitud.getId());
        dto.setIdHecho(solicitud.getHecho().getId());
        dto.setFechaDeCarga(solicitud.getFechaDeCarga());
        dto.setEstado(solicitud.getEstado());
        dto.setTituloNuevo(solicitud.getTituloNuevo());
        dto.setDescripcionNueva(solicitud.getDescripcionNueva());
        dto.setCategoriaNueva(solicitud.getCategoriaNueva());
        dto.setLatitudNueva(solicitud.getLatitudNueva());
        dto.setLongitudNueva(solicitud.getLongitudNueva());
        dto.setFechaHechoNueva(solicitud.getFechaHechoNueva());
        dto.setEtiquetasNuevas(solicitud.getEtiquetasNuevas());
       // dto.setContenidosMultimediaNuevos(solicitud.getContenidosMultimediaNuevos());
        return dto;
    }

}
