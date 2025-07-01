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
    private IAlgoritmoDeConsenso algoritmoDeConsenso;

    private List<String> fuentes;
    private List<Hecho> hechosCargadosManualmente;
    private List<Hecho> hechosConsensuados;

    @Getter(AccessLevel.NONE) // <-
    @Setter(AccessLevel.NONE) // <- Estas cosas son para que no genere getter/setter de esto
    private List<Hecho> hechos;

    public Coleccion(String identificador, String titulo, String descripcion, Criterio criterioDePertenencia, List<String> fuentes, IAlgoritmoDeConsenso algoritmoDeConsenso) {
        this.identificador = identificador;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.criterioDePertenencia = criterioDePertenencia;
        this.fuentes = fuentes;
        this.algoritmoDeConsenso = algoritmoDeConsenso;     // Si, tambien se específica al crear la coleccion
        hechos = new ArrayList<>();
        hechosCargadosManualmente = new ArrayList<>();
        hechosConsensuados = new ArrayList<>();
    }

    public List<Hecho> getHechos() {
        return new ArrayList<Hecho>(hechos); // Creo uno nuevo para que no rompa el Set original si lo modifican
    }
    public List<Hecho> getHechosConsensuados() {
        return new ArrayList<>(hechosConsensuados);
    }

    public ICriterioInmutable getCriterio() {
        return criterioDePertenencia;
    }

    public boolean contiene(Hecho hecho){
        return this.hechos.contains(hecho);
    }

    public boolean cumpleFiltros(Hecho hecho){
        return !aplicarFiltros(List.of(hecho)).isEmpty();
    }

    // TODO hacer que los hechos manuales pasen aun si no cumplen el criterio
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

    public void agregarTandaDeHechos(List<Hecho> tanda) {
        List<Hecho> hechosPotables = aplicarFiltros(tanda);
        List<Hecho> hechosARemover = new ArrayList<>(tanda);
        hechosPotables.forEach(hechosARemover::remove);

        hechosPotables.forEach(h -> {
            if (!contiene(h)) hechos.add(h);
        });
        hechosARemover.forEach(h -> {
           if (contiene(h)) hechos.remove(h);
        });
    }


    public void removerHechoEliminado(Hecho hecho) {
        removerHecho(hecho);
        hechosCargadosManualmente.remove(hecho);
    }

    public void removerHecho(Hecho hecho) {
        hechos.remove(hecho);
    }

    // TODO: Cuándo y cómo deberíamos ver qué hechos permanecen en la colección al actualizar?
    public void agregarHechoManualmente(Hecho hecho) {
        this.hechos.add(hecho);
        this.hechosCargadosManualmente.add(hecho);
    }

    public void quitarHechoManualmente(Hecho hecho) {
        this.removerHecho(hecho);
        hechosCargadosManualmente.remove(hecho);
    }

    public void agregarFuente(String fuente) {
        fuentes.add(fuente);
    }

    public void quitarFuente(String fuente) {
        fuentes.remove(fuente);
    }

    public void consensuarHechos(){
        this.hechosConsensuados.clear();        // Para asegurar que todos los hechos esten consensuados al día, vaciamos la lista
        if(this.algoritmoDeConsenso != null){
            this.hechosConsensuados = this.algoritmoDeConsenso.consensuar(this.hechos, this.fuentes);    // Los volvemos a consensuar
        }else{
            this.hechosConsensuados = this.hechos;      // En caso de que no se aclare un algoritmo de consenso, todos sus hechos catalogarán como "consensuados"
        }
    }
}