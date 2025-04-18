package ar.edu.utn.frba.dds;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;

@AllArgsConstructor
@Builder
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
    private HashSet<String> etiquetas;
    //TODO: contenidoMultimedia
    private OrigenHecho origen;
    private Coordenada lugarAcontecimiento;
    private LocalDate fechaHecho;
    private LocalDateTime fechaDeCarga;
    private HashSet<SolicitudDeEliminacion> solicitudesDeEliminacion;
    private boolean eliminado;      // USO: cuando una solDeElim es aceptada, el hecho se mantiene en el sistema pero no se mostrará en ninguna colección.


    //public Hecho() {

        // Hecho h = new Hecho();
        // h.setTitulo("a");

        // Hecho h2 = Hecho.nuevo()
        //              .conTitulo("a")
        //              .conDescripcion("b")
        //              .conCategoria(new Categoria("c"))
        //              .conLugar(new Coordenada(1,2))
        //              .conFechaDeCargaDesde(LocalDateTime.now())
        //              .conFechaDeCargaHasta(LocalDateTime.now().plusDays(1))
        //              .conFechaDesde(LocalDate.now())
        //              .conFechaHasta(LocalDate.now().plusDays(1))

    //}


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

    public void etiquetar(String etiqueta){
        etiquetas.add(etiqueta);
    }

}