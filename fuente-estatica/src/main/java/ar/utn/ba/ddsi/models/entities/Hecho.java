package ar.utn.ba.ddsi.models.entities;

import ar.utn.ba.ddsi.commons.Coordenada;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Data
// TODO: repasar DDC (constructores del DDC, flechas, etc...)

public class Hecho {
    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private Coordenada lugarAcontecimiento;
    private LocalDate fechaHecho;
    private LocalDateTime fechaDeCarga;
    private Long id; // Identificador dentro del dataset
    private Long idDataset; // Identificador del dataset
    private Boolean eliminado;
}