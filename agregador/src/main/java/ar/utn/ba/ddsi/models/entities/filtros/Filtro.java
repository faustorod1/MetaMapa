package ar.utn.ba.ddsi.models.entities.filtros;

import ar.utn.ba.ddsi.models.entities.Hecho;

import java.util.List;

public abstract class Filtro {
    public abstract List<Hecho> aplicar(List<Hecho> lista);
}