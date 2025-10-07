package ar.utn.ba.ddsi.models.dto.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColeccionOutputDTO {
    private String identificador;
    private String titulo;
    private String descripcion;
    private CriterioOutputDTO criterioDePertenencia;
    private List<FuenteOutputDTO> fuentes;
    private String algoritmoDeConsenso;
}
