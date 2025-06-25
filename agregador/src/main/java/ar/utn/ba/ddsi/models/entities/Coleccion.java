package ar.utn.ba.ddsi.models.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Coleccion {
    private String identificador;       // handle: string alfanumerico (único para cada colección)
    private String titulo;
    private String descripcion;
    private Criterio criterioDePertenencia;

    private List<String> fuentes;
    private List<Hecho> hechosCargadosManualmente;

    @Getter(AccessLevel.NONE) // <-
    @Setter(AccessLevel.NONE) // <- Estas cosas son para que no genere getter/setter de esto
    private List<Hecho> hechos;

    public Coleccion(String identificador, String titulo, String descripcion, Criterio criterioDePertenencia) {
        this.identificador = identificador;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.criterioDePertenencia = criterioDePertenencia;
        hechos = new ArrayList<>();
    }

    public List<Hecho> getHechos() {
        return new ArrayList<Hecho>(hechos); // Creo uno nuevo para que no rompa el Set original si lo modifican
    }

    public boolean contiene(Hecho hecho){
        return this.hechos.contains(hecho);
    }

    public List<Hecho> aplicarFiltros(List<Hecho> hechos) {
        List<Hecho> hechosAFiltrar = hechos
                .stream()
                .filter( h ->
                        fuentes.stream().anyMatch(h::perteneceALaFuente)
                                || hechosCargadosManualmente.stream().anyMatch(hcm -> h.getId().equals(hcm.getId()))
                ).toList();
        return criterioDePertenencia.aplicarA(hechosAFiltrar);
    }

    public void filtrarHechosPropios(List<Hecho> todosLosHechos) {
        this.hechos = aplicarFiltros(todosLosHechos);
    }


    // TODO: Cuándo y cómo deberíamos ver qué hechos permanecen en la colección al actualizar?
    public void agregarHecho(Hecho hecho) {
        this.hechos.add(hecho);
        this.hechosCargadosManualmente.add(hecho);
    }
    public void quitarHecho(Hecho hecho) {
        this.hechosCargadosManualmente.remove(hecho);
        this.hechos.remove(hecho);
    }

    public void agregarFuente(String fuente) {
        fuentes.add(fuente);
    }
}