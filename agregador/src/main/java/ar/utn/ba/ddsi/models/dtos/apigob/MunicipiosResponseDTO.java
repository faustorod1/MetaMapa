package ar.utn.ba.ddsi.models.dtos.apigob;

import ar.utn.ba.ddsi.models.entities.ubicacion.Municipio;
import lombok.Data;

import java.util.List;

@Data
public class MunicipiosResponseDTO {
    private Integer cantidad;
    private Integer inicio;
    private List<Municipio> municipios;
    private Integer total;
}
