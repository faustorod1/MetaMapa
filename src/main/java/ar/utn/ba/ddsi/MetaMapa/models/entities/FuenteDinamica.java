package ar.utn.ba.ddsi.MetaMapa.models.entities;

import java.util.ArrayList;

public class FuenteDinamica implements Fuente {
  private ArrayList<Hecho> hechos;

  public void agregarHecho(Hecho hecho){
    hechos.add(hecho);
  }

  public void modificarHecho(Hecho hechoAmodificar,Hecho hechoNuevo){
    hechos.set(hechos.indexOf(hechoAmodificar), hechoNuevo);
  }

  @Override
  public ArrayList<Hecho> getHechos() {
    return hechos;
  }
}
