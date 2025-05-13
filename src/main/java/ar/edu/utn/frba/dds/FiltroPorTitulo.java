package ar.edu.utn.frba.dds;

import java.util.List;

public class FiltroPorTitulo extends Filtro {
  private String titulo;

  public FiltroPorTitulo(String titulo) {
    this.titulo = titulo;
  }

  @Override
  public List<Hecho> aplicar(List<Hecho> lista) {
    return lista.stream().filter(h -> h.getTitulo().contains(titulo)).toList();
  }
}
