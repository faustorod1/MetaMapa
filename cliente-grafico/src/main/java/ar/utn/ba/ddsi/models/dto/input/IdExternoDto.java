package ar.utn.ba.ddsi.models.dto.input;

import lombok.Value;

import java.io.Serializable;

@Value
public class IdExternoDto implements Serializable {
  FuenteDto fuente;
  Long idExterno;
}