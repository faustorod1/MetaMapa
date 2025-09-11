package ar.utn.ba.ddsi.models.dtos.apigob;

import lombok.Data;

import java.util.List;

@Data
public class GeorefRequestMultipleDTO {
    private List<GeorefRequestDTO> ubicaciones;
}
