package ar.utn.ba.ddsi.models.dto.input;

import lombok.Value;

import java.io.Serializable;

@Value
public class CoordenadaDTO implements Serializable {
  Double latitud;
  Double longitud;
}