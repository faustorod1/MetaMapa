package ar.utn.ba.ddsi.models.dto.output;


import ar.utn.ba.ddsi.models.entities.TipoDeFuente;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FuenteOutputDTO {
    Long id;
    TipoDeFuente tipoDeFuente;
    Long subfuenteId;
}
