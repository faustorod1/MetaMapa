package ar.edu.utn.frba.dds;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@AllArgsConstructor
@Builder
@Getter
@Setter
// TODO: repasar DDC (constructores del DDC, flechas, etc...)

public class Hecho {

    public enum Origen {
        CARGA_MANUAL,
        CONTRIBUYENTE,
        DATASET
    }

    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private ContenidoMultimedia contenidoMultimedia;
    private Origen origen;
    private Coordenada lugarAcontecimiento;
    private LocalDate fechaHecho;
    private LocalDateTime fechaDeCarga;
    private boolean eliminado;      // USO: cuando una solDeElim es aceptada, el hecho se mantiene en el sistema pero no se mostrará en ninguna colección.
    private Contribuyente contribuyente;

    @Builder.Default
    private List<SolicitudDeEliminacion> solicitudesDeEliminacion = new ArrayList<>();
    @Builder.Default //si el builder no le da el valor, hace esto por defecto
    private HashSet<Etiqueta> etiquetas = new HashSet<>();


    /*public void print(){ // Cuando podamos printear vemos de como formatteamos, tal vez como una lista.
        System.out.println("[Titulo: " + titulo + ", ");
        System.out.print("descripcion: " + descripcion + ", ");
        System.out.print("categoria: " + categoria + ", ");
        System.out.print("lugarAcontecimiento: " + lugarAcontecimiento + ", ");
        System.out.print("fechaHecho: " + fechaHecho + ", ");
        System.out.print("fechaDeCarga: " + fechaDeCarga + "]");
    }*/
    // TODO: util para testing, quitar para la entrega

    public SolicitudDeEliminacion solicitarEliminacion(String justificacion, Contribuyente contribuyente) {
        try {
            SolicitudDeEliminacion solicitud = new SolicitudDeEliminacion(this, justificacion, contribuyente);
            solicitudesDeEliminacion.add(solicitud);
            return solicitud;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void etiquetar(Etiqueta etiqueta){
        etiquetas.add(etiqueta);
    }

}