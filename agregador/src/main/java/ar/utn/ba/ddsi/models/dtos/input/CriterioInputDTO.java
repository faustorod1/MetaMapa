package ar.utn.ba.ddsi.models.dtos.input;

import ar.utn.ba.ddsi.models.entities.Criterio;
import lombok.Data;

import java.util.List;

@Data
public class CriterioInputDTO {
    public List<FiltroInputDTO> filtros;

    public Criterio toEntity(){
        Criterio criterio = new Criterio();
        this.getFiltros()
                .stream()
                .map(FiltroInputDTO::toEntity)
                .forEach(criterio::addFiltro);
        return criterio;
    }
}
