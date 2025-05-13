package ar.utn.ba.ddsi.MetaMapa;

import ar.utn.ba.ddsi.MetaMapa.models.entities.*;

import java.util.ArrayList;
import ar.utn.ba.ddsi.MetaMapa.models.entities.*;

public class FuenteDeCargaManual implements Fuente { // Esta es una fuente hecha para los tests de la 1er entrega
  private ArrayList<Hecho> hechos = new ArrayList<Hecho>();

  @Override
  public ArrayList<Hecho> getHechos() {
    return new ArrayList<Hecho>(hechos);
  }

  public void addHecho(Hecho hecho) {
    hechos.add(hecho);
    hecho.setOrigen(OrigenHecho.CARGA_MANUAL);
  }
}
