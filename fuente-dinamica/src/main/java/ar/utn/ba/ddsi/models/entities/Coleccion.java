package ar.utn.ba.ddsi.models.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Coleccion {
    private String titulo;
    private String descripcion;
    private final Fuente fuente;
    private Criterio criterioDePertenencia;

    @Getter(AccessLevel.NONE) // <-
    @Setter(AccessLevel.NONE) // <- Estas cosas son para que no genere getter/setter de esto
    private List<Hecho> hechos;

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

    public boolean contiene(Hecho hecho){
        return this.hechos.contains(hecho);
        //TODO: revisar si este metodo es necesario
    }

    public void recalcularHechos(){
        this.hechos = criterioDePertenencia.aplicarA(fuente.getHechos());
    }
}