package ar.utn.ba.ddsi.models.dto.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudDeEliminacionOutputDTO {
    private String descripcion;
    private Long hechoId;

}
