package ar.utn.ba.ddsi.models.entities;

import ar.utn.ba.ddsi.models.entities.filtros.Filtro;

import java.util.List;

public interface ICriterioInmutable {
    List<Filtro> getFiltros();
    List<Hecho> aplicarA(List<Hecho> listaOriginal);
}
