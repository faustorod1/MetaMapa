package ar.utn.ba.ddsi.models.entities;

import java.util.List;

public class FiltroPorFuentes extends Filtro {
  // ej. "dinamica", "proxy", "proxy:3"
  private List<String> fuentes;

  public List<Hecho> aplicar(List<Hecho> lista) {
    return lista
        .stream()
        .filter( h ->
        fuentes
            .stream()
            .anyMatch(f ->
            h.getIdExterno().contains(f)
        )
    ).toList();
  }
}