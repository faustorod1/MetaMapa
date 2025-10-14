package ar.utn.ba.ddsi.models.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaDTO implements Serializable {
  Long id;
  String nombre;
  List<String> sinonimos;
}