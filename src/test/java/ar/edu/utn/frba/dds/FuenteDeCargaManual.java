package ar.edu.utn.frba.dds;

import java.util.ArrayList;
import java.util.List;

public class FuenteDeCargaManual implements Fuente {//Esta es una fuente hecha para los tests de la 1er entrega
  private List<Hecho> hechos = new ArrayList<Hecho>();

  @Override
  public List<Hecho> getHechos() {
    return new ArrayList<Hecho>(hechos);
  }

  public void addHecho(Hecho hecho) {
    hechos.add(hecho);
  }
}
