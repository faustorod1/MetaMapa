package ar.utn.ba.ddsi.models.dtos.output;

import lombok.Data;

import java.util.List;

@Data
public class CriterioOutputDTO {
    private List<FiltroOutputDTO> filtros;

}
