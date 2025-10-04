package ar.utn.ba.ddsi.models.entities;

import lombok.Data;

@Data
public class DatosRegister {
  private String nombre;
  private String apellido;
  private String email;
  private String contrasenia;
}
