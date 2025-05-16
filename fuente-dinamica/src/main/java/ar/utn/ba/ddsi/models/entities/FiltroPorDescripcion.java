package ar.utn.ba.ddsi.models.entities;

import java.util.ArrayList;
import java.util.List;

public class FiltroPorDescripcion extends Filtro{
    private String descripcion;

    public FiltroPorDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public List<Hecho> aplicar(List<Hecho> lista) {
        List<Hecho> filtrados = new ArrayList<>(lista);
        filtrados = lista.stream().filter(h -> h.getDescripcion().contains(descripcion)).toList();

        return filtrados;
    }
}