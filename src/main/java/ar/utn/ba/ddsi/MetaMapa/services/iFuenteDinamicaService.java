package ar.utn.ba.ddsi.MetaMapa.services;

import ar.utn.ba.ddsi.MetaMapa.models.entities.Hecho;

import java.util.List;

public interface iFuenteDinamicaService {
  void crearHecho(Hecho hecho);
  void modificarHecho(Hecho hechoAModificar,Hecho hechoNuevo);
  List<Hecho> obtenerHechos(Boolean pendiente);
  void procesarPendientes();
}
