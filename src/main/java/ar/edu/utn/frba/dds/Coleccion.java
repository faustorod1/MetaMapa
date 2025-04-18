package ar.edu.utn.frba.dds;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Coleccion {
    private String titulo;
    private String descripcion;

    @Getter(AccessLevel.NONE) // <-
    @Setter(AccessLevel.NONE) // <- Estas cosas son para que no genere getter/setter de esto
    private List<Hecho> hechos;

    private final Fuente fuente;
    private Criterio criterioDePertenencia;

    public Coleccion(String titulo, String descripcion, Fuente fuente,Criterio criterioDePertenencia) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fuente = fuente;
        this.criterioDePertenencia = criterioDePertenencia;
        this.hechos = criterioDePertenencia.aplicarA(fuente.getHechos());
    }

    public List<Hecho> getHechos() {
        return new ArrayList<Hecho>(hechos); // Creo uno nuevo para que no rompa el Set original si lo modifican
    }

    public void agregarHecho(Hecho hecho){
        hechos.add(hecho);
    }

    public void eliminarHecho(Hecho hecho){
        hechos.remove(hecho);
    }

    public boolean pertenece(Hecho hecho){
        return this.hechos.contains(hecho);
    }

    // TODO: adaptar todo el DDC

}