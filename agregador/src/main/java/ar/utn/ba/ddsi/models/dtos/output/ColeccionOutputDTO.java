package ar.utn.ba.ddsi.models.dtos.output;

import ar.utn.ba.ddsi.models.entities.Criterio;
import ar.utn.ba.ddsi.models.entities.Hecho;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class ColeccionOutputDTO {
    private String identificador;
    private String titulo;
    private String descripcion;
    private CriterioOutputDTO criterioDePertenencia;
}
