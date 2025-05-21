package ar.utn.ba.ddsi.models.entities;

import lombok.Getter;

import java.util.List;

public class FiltroPorTitulo extends Filtro {
    @Getter
    private String titulo;

    public FiltroPorTitulo(String titulo) {
        this.titulo = titulo;
    }

    @Override
    public List<Hecho> aplicar(List<Hecho> lista) {
        return lista.stream().filter(h -> h.getTitulo().contains(titulo)).toList();
    }
}