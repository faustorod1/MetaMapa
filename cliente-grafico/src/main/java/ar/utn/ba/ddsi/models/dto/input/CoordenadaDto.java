package ar.utn.ba.ddsi.models.dto.input;

import lombok.Value;

import java.io.Serializable;

@Value
public class CoordenadaDto implements Serializable {
  Double latitud;
  Double longitud;
}