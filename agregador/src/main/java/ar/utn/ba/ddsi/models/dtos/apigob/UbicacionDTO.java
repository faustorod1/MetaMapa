package ar.utn.ba.ddsi.models.dtos.apigob;

import lombok.Data;

import java.util.List;

@Data
public class UbicacionDTO{
  private String provinicia_nombre;
  private String municipio_nombre;
  private Double lat;
  private Double lon;
}
