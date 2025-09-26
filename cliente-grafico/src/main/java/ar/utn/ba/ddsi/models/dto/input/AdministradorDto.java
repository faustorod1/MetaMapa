package ar.utn.ba.ddsi.models.dto.input;

import lombok.Value;

import java.io.Serializable;

@Value
public class AdministradorDto implements Serializable {
  String nombre;
  String apellido;
}