package ar.edu.utn.frba.dds;

import java.util.List;

public class FiltroPorEliminados extends Filtro {

  @Override
  public List<Hecho> aplicar(List<Hecho> lista) {
    return lista.stream().filter(h -> !h.isEliminado()).toList();
  }
}
