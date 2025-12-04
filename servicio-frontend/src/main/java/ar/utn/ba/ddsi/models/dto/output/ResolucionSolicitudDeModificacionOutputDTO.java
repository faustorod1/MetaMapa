package ar.utn.ba.ddsi.models.dto.output;

import ar.utn.ba.ddsi.models.entities.EstadoSolicitud;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResolucionSolicitudDeModificacionOutputDTO {
    private String motivoDeEstado;
    private EstadoSolicitud estadoNuevo;


}
