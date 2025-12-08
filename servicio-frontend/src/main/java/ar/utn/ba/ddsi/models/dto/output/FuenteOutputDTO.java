package ar.utn.ba.ddsi.models.dto.output;


import ar.utn.ba.ddsi.models.dto.input.FuenteDTO;
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

    public static FuenteOutputDTO fromDTOtoOutput(FuenteDTO dto) {
        return FuenteOutputDTO.builder()
                .id(dto.getId())
                .tipoDeFuente(dto.getTipoDeFuente())
                .subfuenteId(dto.getSubfuenteId())
                .build();
    }
}
