package ar.utn.ba.ddsi.models.entities.filtros;

import ar.utn.ba.ddsi.models.entities.Hecho;

import java.util.List;

public class FiltroPorEliminados extends Filtro {

    @Override
    public List<Hecho> aplicar(List<Hecho> lista) {
        return lista.stream().filter(h -> !h.isEliminado()).toList();
    }
}