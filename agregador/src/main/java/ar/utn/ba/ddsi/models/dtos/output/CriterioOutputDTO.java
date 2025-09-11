package ar.utn.ba.ddsi.models.dtos.output;

import ar.utn.ba.ddsi.models.entities.Criterio;
import lombok.Data;

import java.util.List;

@Data
public class CriterioOutputDTO {
    private List<FiltroOutputDTO> filtros;

    public static CriterioOutputDTO fromEntity(Criterio criterio) {
        CriterioOutputDTO dto = new CriterioOutputDTO();
        dto.setFiltros(
                criterio.getFiltros()
                        .stream()
                        .map(FiltroOutputDTO::fromEntity)
                        .toList()
        );
        return dto;
    }
}
