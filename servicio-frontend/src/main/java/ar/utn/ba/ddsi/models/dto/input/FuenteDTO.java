package ar.utn.ba.ddsi.models.dto.input;

import ar.utn.ba.ddsi.models.entities.TipoDeFuente;
import lombok.Value;

import java.io.Serializable;

@Value
public class FuenteDTO implements Serializable {
  Long id;
  TipoDeFuente tipoDeFuente;
  Long subfuenteId;
}