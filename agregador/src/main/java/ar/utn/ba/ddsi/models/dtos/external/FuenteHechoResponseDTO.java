package ar.utn.ba.ddsi.models.dtos.external;

import lombok.Data;

import java.util.List;

@Data
public class FuenteHechoResponseDTO {
    List<FuenteHechoDTO> hechos;
}
