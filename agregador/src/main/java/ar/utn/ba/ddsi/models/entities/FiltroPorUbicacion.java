package ar.utn.ba.ddsi.models.entities;

import lombok.Getter;
import ar.utn.ba.ddsi.commons.Coordenada;

import java.util.ArrayList;
import java.util.List;

public class FiltroPorUbicacion extends Filtro {
    @Getter
    private Coordenada lugar;

    public FiltroPorUbicacion(Coordenada lugar) {
        this.lugar = lugar;
    }

    @Override
    public List<Hecho> aplicar(List<Hecho> lista){
        List<Hecho> filtrados = new ArrayList<>(lista);
        filtrados = lista.stream().filter(h -> h.getLugarAcontecimiento().equals(lugar)).toList();
        return filtrados;
    }


    //TODO: implementar un algoritmo q filtre por un radio cercano a una coordenada en vez de q sean literalmente iguales
}