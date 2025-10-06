package ar.utn.ba.ddsi.models.dto.input;

import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;

@Value
public class ContribuyenteDTO implements Serializable {
  Long id;
  String nombre;
  String apellido;
  LocalDate fechaNacimiento;
}