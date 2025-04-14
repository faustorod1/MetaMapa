package ar.edu.utn.frba.dds;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;

@Getter
public class Coleccion {
    private String titulo;
    private String descripcion;

    @Getter(AccessLevel.NONE) // <-
    @Setter(AccessLevel.NONE) // <- Estas cosas son para que no genere getter/setter de esto
    private HashSet<Hecho> hechos;

    private final Fuente fuente;
    private int criterioDePertenencia; // TODO: Cambiar, no debería ser int

    public Coleccion(String titulo, String descripcion, Fuente fuente) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fuente = fuente;
        //TODO: setear criterio
    }

    public HashSet<Hecho> getHechos() {
        return new HashSet<>(hechos); // Creo uno nuevo para que no rompa el Set original si lo modifican
    }

    public void agregarHecho(Hecho hecho){
        hechos.add(hecho);
    }

    public void navegarHechos(){
        hechos.forEach(Hecho::print);
    }

    public void navegarHechosFiltrados(){
    }

    public void eliminarHecho(Hecho hecho){
        hechos.remove(hecho);
    }

    // TODO: adaptar todo el DDC

}