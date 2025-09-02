package ar.utn.ba.ddsi.models.dtos.externals;

import ar.utn.ba.ddsi.models.entities.Categoria;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class HechoExternalMetamapaDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private String categoria;
    //private String contenidoMultimedia;
    private Double latitud;
    private Double longitud;
    private LocalDateTime fechaHecho;
    private LocalDateTime fechaDeCarga;
    private LocalDateTime fechaUltimaActualizacion;
}
