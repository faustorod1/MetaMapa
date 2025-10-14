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
    private Long id;
    private String tipoDeFuente;
    private Long subfuenteId;
    private String titulo;
    private String descripcion;
    private String categoria;
    private List<String> contenidoMultimedia;
    private OrigenHecho origen;
    private Coordenada lugarAcontecimiento;
    private LocalDateTime fechaHecho;
    private LocalDateTime fechaDeCarga;
    private LocalDateTime fechaUltimaActualizacion;
    private boolean eliminado;
    private Long contribuyenteId;
    private HashSet<String> etiquetas;

    public static HechoOutputDTO fromEntity(Hecho hecho) {
        return HechoOutputDTO.builder()
                .id(hecho.getId())
                .subfuenteId(hecho.getAPIid())
                .tipoDeFuente("PROXY")
                .titulo(hecho.getTitulo())
                .descripcion(hecho.getDescripcion())
                .categoria(hecho.getCategoria())
                .origen(OrigenHecho.PROXY)
                .lugarAcontecimiento(hecho.getLugarAcontecimiento())
                .fechaHecho(hecho.getFechaHecho())
                .fechaDeCarga(hecho.getFechaDeCarga())
                .fechaUltimaActualizacion(hecho.getFechaUltimaActualizacion())
                .eliminado(hecho.isEliminado())
                .build();
    }
}
