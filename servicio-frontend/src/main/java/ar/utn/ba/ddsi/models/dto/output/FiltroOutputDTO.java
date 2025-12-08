package ar.utn.ba.ddsi.models.dto.output;

import ar.utn.ba.ddsi.models.dto.input.FiltroDTO;
import lombok.*;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FiltroOutputDTO {
    private String tipoDeFiltro;
    private Map<String,Object> parametros;


    public static FiltroOutputDTO fromDTOtoOutput(FiltroDTO filtro){
        return FiltroOutputDTO.builder()
                .tipoDeFiltro(filtro.getTipoDeFiltro())
                .parametros(filtro.getParametros())
                .build();
    }
}
