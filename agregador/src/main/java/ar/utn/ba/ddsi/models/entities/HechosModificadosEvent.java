package ar.utn.ba.ddsi.models.entities;

import java.util.List;

public class HechosModificadosEvent {
  private final List<Hecho> hechos;

  public HechosModificadosEvent(List<Hecho> hechos){
    this.hechos = hechos;
  }

  public List<Hecho> getHechos(){
    return hechos;
  }
}
