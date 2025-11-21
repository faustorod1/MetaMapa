package ar.utn.ba.ddsi.models.dto.output;

import ar.utn.ba.ddsi.models.dto.input.EtiquetaDTO;
import ar.utn.ba.ddsi.models.dto.input.HechoDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class EtiquetaOutputDTO {
    private String nombre;

}
