package ar.utn.ba.ddsi.models.dtos.output;

import lombok.Data;

import java.util.Map;

@Data
public class FiltroOutputDTO {
    private String tipoDeFiltro;
    private Map<String,Object> parametros;
}
