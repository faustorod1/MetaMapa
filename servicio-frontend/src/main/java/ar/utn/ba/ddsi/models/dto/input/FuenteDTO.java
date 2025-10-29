package ar.utn.ba.ddsi.models.dto.input;

import ar.utn.ba.ddsi.models.entities.TipoDeFuente;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FuenteDTO implements Serializable {
  Long id;
  TipoDeFuente tipoDeFuente;
  Long subfuenteId;
}