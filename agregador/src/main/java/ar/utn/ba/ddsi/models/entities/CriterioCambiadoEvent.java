package ar.utn.ba.ddsi.models.entities;


import lombok.Getter;

@Getter
public class CriterioCambiadoEvent {
  public Coleccion coleccion;

  public CriterioCambiadoEvent(Coleccion coleccion) {
    this.coleccion = coleccion;
  }

}
