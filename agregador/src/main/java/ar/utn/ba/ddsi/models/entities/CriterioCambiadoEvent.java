package ar.utn.ba.ddsi.models.entities;


public class CriterioCambiadoEvent {
  public Coleccion coleccion;

  public CriterioCambiadoEvent(Coleccion coleccion) {
    this.coleccion = coleccion;
  }

  public Coleccion getColeccion() {
    return coleccion;
  }
}
