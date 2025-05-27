package ar.utn.ba.ddsi.models.entities;

import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

public class FiltroPorCategoria extends Filtro{
    @Getter
    private Categoria categoria;

    public FiltroPorCategoria(Categoria categoria){
        this.categoria = categoria;
    }

    @Override
    public List<Hecho> aplicar(List<Hecho> hechos){
        return hechos.stream().filter(hecho -> hecho.getCategoria().equals(categoria)).collect(Collectors.toList());
    }

}