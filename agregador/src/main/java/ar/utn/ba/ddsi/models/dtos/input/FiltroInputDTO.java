package ar.utn.ba.ddsi.models.dtos.input;

import lombok.Data;

import java.util.Map;

@Data
public class FiltroInputDTO {
    private String tipoDeFiltro;
    private Map<String,Object> parametros;
}