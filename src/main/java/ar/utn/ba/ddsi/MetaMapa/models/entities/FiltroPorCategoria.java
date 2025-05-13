package ar.utn.ba.ddsi.MetaMapa.models.entities;

import java.util.List;
import java.util.stream.Collectors;

public class FiltroPorCategoria extends Filtro{
    private Categoria categoria;

    public FiltroPorCategoria(Categoria categoria){
        this.categoria = categoria;
    }

    @Override
    public List<Hecho> aplicar(List<Hecho> hechos){
        return hechos.stream().filter(hecho -> hecho.getCategoria() == categoria).collect(Collectors.toList());
    }

}