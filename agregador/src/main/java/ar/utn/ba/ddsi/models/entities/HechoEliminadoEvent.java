package ar.utn.ba.ddsi.models.entities;

public class HechoEliminadoEvent {
  public Hecho hechoEliminado;

  public HechoEliminadoEvent(Hecho hechoEliminado) {
    this.hechoEliminado = hechoEliminado;
  }

  public Hecho getHechoEliminado() {
    return hechoEliminado;
  }
}
