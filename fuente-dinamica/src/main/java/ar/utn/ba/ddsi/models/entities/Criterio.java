package ar.utn.ba.ddsi.models.entities;

import java.util.ArrayList;
import java.util.List;


public class Criterio {
    private List<Filtro> filtros = new ArrayList<>();

    public Criterio(){
        filtros.add(new FiltroPorEliminados());
    }

    public static Criterio nuevo() {
        return new Criterio();
    }

    public Criterio addFiltro(Filtro filtro) {
        filtros.add(filtro);
        return this;
    }

    public List<Hecho> aplicarA(List<Hecho> listaOriginal){
        List<Hecho> hechos = new ArrayList<>(listaOriginal);

        for (Filtro filtro : filtros) {
            hechos = filtro.aplicar(hechos);
        }
        return hechos;
    }

}