package ar.edu.utn.frba.dds;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class Fuente {
  private List<Hecho> hechos;

  public List<Hecho> getHechos(){
    return new ArrayList<Hecho>(hechos);
  }

}
