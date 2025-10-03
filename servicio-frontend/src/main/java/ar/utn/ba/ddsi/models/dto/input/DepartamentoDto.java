package ar.utn.ba.ddsi.models.dto.input;

import lombok.Value;

import java.io.Serializable;

@Value
public class DepartamentoDto implements Serializable {
  Long id;
  String nombre;
  ProvinciaDto provincia;
}