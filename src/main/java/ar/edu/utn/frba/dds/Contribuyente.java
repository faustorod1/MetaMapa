package ar.edu.utn.frba.dds;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Contribuyente{
  private String nombre;
  private String apellido;
  private int edad;

  public Contribuyente(String nombre, String apellido, int edad) {
    this.nombre = nombre;
    this.apellido = apellido;
    this.edad = edad;
  }
}
