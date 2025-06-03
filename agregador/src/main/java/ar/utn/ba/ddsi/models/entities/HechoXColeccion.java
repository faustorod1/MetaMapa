package ar.utn.ba.ddsi.models.entities;

import lombok.Data;

@Data
public class HechoXColeccion {
  String coleccionIdentificador;
  Long hechoId;

  public HechoXColeccion(String coleccionIdentificador, Long hechoId) {
    this.coleccionIdentificador = coleccionIdentificador;
    this.hechoId = hechoId;
  }
}