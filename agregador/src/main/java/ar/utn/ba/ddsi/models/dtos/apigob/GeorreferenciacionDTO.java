package ar.utn.ba.ddsi.models.dtos.apigob;

import lombok.Data;

import java.util.List;

@Data
public class GeorreferenciacionDTO {
  private List<ResultadoGeoDTO> resultados;
}
