package ar.utn.ba.ddsi.models.dtos.apigob;

import ar.utn.ba.ddsi.models.entities.ubicacion.Departamento;
import lombok.Data;

import java.util.List;

@Data
public class DepartamentosResponseDTO {
    private Integer cantidad;
    private Integer inicio;
    private List<Departamento> departamentos;
    private Integer total;
}
