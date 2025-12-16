package ar.utn.ba.ddsi.models.dtos.external;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdExternoDTO {
    private Long id;
    private Long subfuenteId;
}
