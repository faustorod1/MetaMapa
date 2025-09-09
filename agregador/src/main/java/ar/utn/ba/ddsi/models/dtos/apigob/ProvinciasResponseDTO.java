package ar.utn.ba.ddsi.models.dtos.apigob;

import ar.utn.ba.ddsi.models.entities.ubicacion.Provincia;
import lombok.Data;

import java.util.List;

@Data
public class ProvinciasResponseDTO {
    private List<Provincia> provincias;
}
