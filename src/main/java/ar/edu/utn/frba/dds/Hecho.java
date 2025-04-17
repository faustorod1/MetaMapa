package ar.edu.utn.frba.dds;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Getter
@Setter

// TODO: repasar DDC (constructores del DDC, verificación de flechas, etc...)

public class Hecho {

    public enum Campos {
        TITULO, DESCRIPCION, CATEGORIA, LATITUD, LONGITUD, FECHADEHECHO
    }

    private String titulo;
    private String descripcion;
    private Categoria categoria;
    //TODO: contenidoMultimedia
    //TODO: private Fuente origen
    private Coordenada lugarAcontecimiento;
    private LocalDate fechaHecho;
    private LocalDateTime fechaDeCarga;
    private HashSet<SolicitudDeEliminacion> solicitudesDeEliminacion;
    private boolean eliminado;      // USO: cuando una solDeElim es aceptada, el hecho se mantiene en el sistema pero no se mostrará en ninguna colección.

    public void print(){ // Cuando podamos printear vemos de como formatteamos, tal vez como una lista.
        System.out.println("[Titulo: " + titulo + ", ");
        System.out.print("descripcion: " + descripcion + ", ");
        System.out.print("categoria: " + categoria + ", ");
        System.out.print("lugarAcontecimiento: " + lugarAcontecimiento + ", ");
        System.out.print("fechaHecho: " + fechaHecho + ", ");
        System.out.print("fechaDeCarga: " + fechaDeCarga + "]");
    }

    public void solicitarEliminacion(String justificacion) {
        solicitudesDeEliminacion.add(new SolicitudDeEliminacion(this, justificacion));
    }
}