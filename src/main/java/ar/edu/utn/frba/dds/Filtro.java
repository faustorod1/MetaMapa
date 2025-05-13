package ar.edu.utn.frba.dds;

import java.util.ArrayList;
import java.util.List;

public abstract class Filtro {

  public abstract List<Hecho> aplicar(List<Hecho> lista);
}

