package ar.utn.ba.ddsi.models.dtos.inputs;

import lombok.Data;

import java.util.List;

@Data
public class ColeccionInputDTO {
    private String identificador;
    private String titulo;
    private String descripcion;
    private CriterioInputDTO criterioDePertenencia;
    private List<String> fuentes;
}
