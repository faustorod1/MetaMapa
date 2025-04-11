package ar.edu.utn.frba.dds;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;

@Getter
@Setter

public class Hecho {

    public enum Campos {
        TITULO, DESCRIPCION, CATEGORIA, LATITUD, LONGITUD, FECHADEHECHO
    }

    private String titulo;
    private String descripcion;
    private Categoria categoria;

    //TODO: contenidoMultimedia;
    //TODO: private Fuente origen:

    private Coordenada lugarAcontecimiento;
    private LocalDate fechaHecho;
    private LocalDateTime fechaDeCarga;
    private HashSet<SolicitudDeEliminacion> solicitudesDeEliminacion;
    private boolean eliminado;      // USO: cuando una solDeElim es aceptada, el hecho se mantiene en el sistema pero no se mostrará en ninguna colección.

