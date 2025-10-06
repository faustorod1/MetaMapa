package ar.utn.ba.ddsi.models.dto.output;

import lombok.Value;

import java.io.Serializable;

@Value
public class DatosLoginDTO implements Serializable {
  String email;
  String password;
}