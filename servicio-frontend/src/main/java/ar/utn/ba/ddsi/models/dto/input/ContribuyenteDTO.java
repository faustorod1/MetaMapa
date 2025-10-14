package ar.utn.ba.ddsi.models.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContribuyenteDTO implements Serializable {
  Long id;
  String nombre;
  String apellido;
  LocalDate fechaNacimiento;
}