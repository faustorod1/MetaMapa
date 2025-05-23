package ar.utn.ba.ddsi.models.dtos.outputs;

import ar.utn.ba.ddsi.models.entities.Categoria;
import ar.utn.ba.ddsi.models.entities.Coordenada;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;


@AllArgsConstructor
@Builder
@Data
public class HechoOutputDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private Coordenada coordenada;
    private LocalDate fechaHecho;
    private LocalDateTime fechaDeCarga;
    private LocalDateTime lastUpdate;

}