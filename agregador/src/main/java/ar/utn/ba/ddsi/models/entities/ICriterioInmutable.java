package ar.utn.ba.ddsi.models.entities;

import java.util.List;

public interface ICriterioInmutable {
    List<Filtro> getFiltros();
    List<Hecho> aplicarA(List<Hecho> listaOriginal);
}
