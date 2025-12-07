package ar.utn.ba.ddsi.models.dto.output;

import lombok.*;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FiltroOutputDTO {
    private String tipoDeFiltro;
    private Map<String,Object> parametros;

}
