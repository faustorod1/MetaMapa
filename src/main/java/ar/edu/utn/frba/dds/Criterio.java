package ar.edu.utn.frba.dds;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Criterio {

  private ArrayList<Predicate<Hecho>> filtros = new ArrayList<>();

  public Criterio() {
    filtros.add(h -> !h.isEliminado());
  }

  public static Criterio nuevo() {
    return new Criterio();
  }

  public List<Hecho> aplicarA(List<Hecho> listaOriginal){
    Predicate<Hecho> filtrosUnificados = filtros.stream().reduce(x -> true, Predicate::and);
    return listaOriginal.stream().filter(filtrosUnificados).collect(Collectors.toList());
  }


  // FILTROS
  public Criterio conCategoria(Categoria categoria) {
    filtros.add(h -> h.getCategoria() == categoria);
    return this;
  }

  public Criterio conFechaDeCargaDesde(LocalDateTime desde) {
    filtros.add(h -> h.getFechaDeCarga().isAfter(desde) || h.getFechaDeCarga().isEqual(desde));
    return this;
  }

  public Criterio conFechaDeCargaHasta(LocalDateTime hasta) {
    filtros.add(h -> h.getFechaDeCarga().isBefore(hasta) || h.getFechaDeCarga().isEqual(hasta));
    return this;
  }

  public Criterio conFechaDesde(LocalDate desde) {
    filtros.add(h -> h.getFechaHecho().isAfter(desde) || h.getFechaHecho().isEqual(desde));
    return this;
  }

  public Criterio conFechaHasta(LocalDate hasta) {
    filtros.add(h -> h.getFechaHecho().isBefore(hasta) || h.getFechaHecho().isEqual(hasta));
    return this;
  }

  public Criterio conTitulo(String titulo) {
  filtros.add(h -> h.getTitulo().toLowerCase().contains(titulo.toLowerCase()));
    return this;
  }

  public Criterio conDescripcion(String descripcion) {
    filtros.add(h -> h.getDescripcion().toLowerCase().contains(descripcion.toLowerCase()));
    return this;
  }

  public Criterio conLugar(Coordenada lugar) {
    filtros.add(h -> h.getLugarAcontecimiento().equals(lugar));
    return this;
    //TODO: quizas dos lugares debemos considerarlos como iguales si estan cerquita (pertenecen a una misma zona) y no si obligatoriamente tienen las mismas coordenadas exactamente
  }
}
