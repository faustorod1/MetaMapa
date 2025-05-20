package ar.utn.ba.ddsi.models.dtos.output;

import ar.utn.ba.ddsi.models.entities.Filtro;
import lombok.Data;

import java.util.List;

@Data
public class CriterioOutputDTO {
    private List<Filtro> filtros;
}
