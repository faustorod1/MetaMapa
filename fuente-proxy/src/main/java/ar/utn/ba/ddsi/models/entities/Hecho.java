package ar.utn.ba.ddsi.models.entities;

import ar.utn.ba.ddsi.commons.Coordenada;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@AllArgsConstructor
@Builder
@Data

public class Hecho {
    private Long id;
    private Long APIid;
    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private ContenidoMultimedia contenidoMultimedia;
    private Coordenada lugarAcontecimiento;
    private LocalDateTime fechaHecho;
    private LocalDateTime fechaDeCarga;
    private LocalDateTime fechaUltimaActualizacion;
    private boolean eliminado;      // USO: cuando una solDeElim es aceptada, el hecho se mantiene en el sistema pero no se mostrará en ninguna colección.
}