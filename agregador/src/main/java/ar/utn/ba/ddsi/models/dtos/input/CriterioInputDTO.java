package ar.utn.ba.ddsi.models.dtos.input;

import lombok.Data;

import java.util.List;

@Data
public class CriterioInputDTO {
    public List<FiltroInputDTO> filtros;
}
