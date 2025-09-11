package ar.utn.ba.ddsi.models.dtos.apigob;

import ar.utn.ba.ddsi.commons.Coordenada;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GeorefRequestDTO {
    private Double lat;
    private Double lon;
    private Boolean aplanar;
    private String campos;

    public static GeorefRequestDTO fromCoordenada(Coordenada coordenada) {
        return GeorefRequestDTO.builder()
                .lat(coordenada.getLatitud())
                .lon(coordenada.getLongitud())
                .aplanar(true)
                .campos("estandar")
                .build();
    }
}