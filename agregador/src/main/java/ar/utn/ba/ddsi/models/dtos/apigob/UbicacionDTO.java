package ar.utn.ba.ddsi.models.dtos.apigob;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class UbicacionDTO{
  @JsonProperty("provincia_nombre")
  private String provincia_nombre;
  @JsonProperty("departamento_nombre")
  private String departamento_nombre;
  private Double lat;
  private Double lon;
}
