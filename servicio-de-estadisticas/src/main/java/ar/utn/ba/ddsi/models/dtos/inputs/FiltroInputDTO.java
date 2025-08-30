package ar.utn.ba.ddsi.models.dtos.inputs;

import lombok.Data;

import java.util.Map;

@Data
public class FiltroInputDTO {
    private String tipoDeFiltro;
    private Map<String,Object> parametros;
}
