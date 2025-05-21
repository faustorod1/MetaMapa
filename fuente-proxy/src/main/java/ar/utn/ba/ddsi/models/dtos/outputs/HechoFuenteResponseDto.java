package ar.utn.ba.ddsi.models.dtos.outputs;

import lombok.Data;

import java.util.List;

@Data
public class HechoFuenteResponseDto {
  private List<HechoDTO> hechos;
}
