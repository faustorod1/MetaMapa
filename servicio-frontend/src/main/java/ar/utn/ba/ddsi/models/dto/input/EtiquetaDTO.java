package ar.utn.ba.ddsi.models.dto.input;

import lombok.Data;
import lombok.Value;

import java.io.Serializable;

@Value
public class EtiquetaDTO implements Serializable {
  long id;
  String nombre;
}