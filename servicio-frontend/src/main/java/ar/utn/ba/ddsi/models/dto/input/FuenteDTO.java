package ar.utn.ba.ddsi.models.dto.input;

import lombok.Value;

import java.io.Serializable;

@Value
public class FuenteDTO implements Serializable {
  Long id;
  ar.utn.ba.ddsi.models.entities.TipoDeFuente tipoDeFuente;
  Long subfuenteId;
}