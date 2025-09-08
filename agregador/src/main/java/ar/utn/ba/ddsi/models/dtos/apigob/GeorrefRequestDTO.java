package ar.utn.ba.ddsi.models.dtos.apigob;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GeorrefRequestDTO {
    private Double lat;
    private Double lon;
    private Boolean aplanar;
    private String campos;
}