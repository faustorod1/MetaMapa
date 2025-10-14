package ar.utn.ba.ddsi.models.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProvinciaDTO implements Serializable {
  Long id;
  String nombre;
}