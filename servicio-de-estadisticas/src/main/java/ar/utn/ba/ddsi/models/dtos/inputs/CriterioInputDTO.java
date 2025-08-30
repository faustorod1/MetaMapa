package ar.utn.ba.ddsi.models.dtos.inputs;

import lombok.Data;

import java.util.List;

@Data
public class CriterioInputDTO {
    private List<FiltroInputDTO> filtros;

}
