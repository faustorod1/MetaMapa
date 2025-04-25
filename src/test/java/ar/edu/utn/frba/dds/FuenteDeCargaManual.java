package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.fuentes.Fuente;
import ar.edu.utn.frba.dds.hechos.Hecho;

import java.util.ArrayList;

public class FuenteDeCargaManual implements Fuente { // Esta es una fuente hecha para los tests de la 1er entrega
  private ArrayList<Hecho> hechos = new ArrayList<Hecho>();

  @Override
  public ArrayList<Hecho> getHechos() {
    return new ArrayList<Hecho>(hechos);
  }

  public void addHecho(Hecho hecho) {
    hechos.add(hecho);
    hecho.setOrigen(Hecho.Origen.CARGA_MANUAL);
  }
}
