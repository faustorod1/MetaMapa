package ar.utn.ba.ddsi.models.dto.input;

import lombok.Data;

import java.util.List;

@Data
public class CriterioDTO {
    private List<FiltroDTO> filtros;
}
