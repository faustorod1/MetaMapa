package ar.utn.ba.ddsi.models.dto.output;

import ar.utn.ba.ddsi.models.dto.input.CriterioDTO;
import ar.utn.ba.ddsi.models.dto.input.FiltroDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CriterioOutputDTO {
    public List<FiltroOutputDTO> filtros = new ArrayList<>();


    public static CriterioOutputDTO fromDTOtoOutput (CriterioDTO criterio){
        List<FiltroOutputDTO> filtrosOutput = criterio.getFiltros().stream().map(FiltroOutputDTO::fromDTOtoOutput).toList();

        return CriterioOutputDTO.builder()
                .filtros(filtrosOutput)
                .build();
    }
}
