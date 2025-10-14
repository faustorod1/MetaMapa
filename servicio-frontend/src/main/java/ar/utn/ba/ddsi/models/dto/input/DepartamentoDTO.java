package ar.utn.ba.ddsi.models.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartamentoDTO implements Serializable {
  Long id;
  String nombre;
  ProvinciaDTO provincia;
}