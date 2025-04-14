package ar.edu.utn.frba.dds;

public interface Criterio {
  //Pense en Patron State. Hacer una Clase q herede para cada criterio deseado y definirle la implemetacion a "pertenece"
  public boolean pertenece(Hecho hecho, Coleccion coleccion);

}