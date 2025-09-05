package ar.utn.ba.ddsi.models.dtos.apigob;

import lombok.Builder;

@Builder
public class GeorrefRequestDTO {
    private Double lat;
    private Double lon;
    private Boolean aplanar;
    private String campo;
}