package ar.utn.ba.ddsi.models.dto.input;

import lombok.Value;

import java.io.Serializable;

@Value
public class IdExternoDTO implements Serializable {
  FuenteDTO fuente;
  Long idExterno;
}