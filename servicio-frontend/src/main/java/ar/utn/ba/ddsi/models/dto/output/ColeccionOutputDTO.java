package ar.utn.ba.ddsi.models.dto.output;

import ar.utn.ba.ddsi.models.dto.input.ColeccionDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

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


    public static ColeccionOutputDTO fromDTOtoOutput(ColeccionDTO dto) {
        List<FuenteOutputDTO> fuentesOutput = dto.getFuentes().stream()
                .map(FuenteOutputDTO::fromDTOtoOutput)
                .toList();

        return ColeccionOutputDTO.builder()
                .identificador(dto.getIdentificador())
                .titulo(dto.getTitulo())
                .descripcion(dto.getDescripcion())
                .algoritmoDeConsenso(dto.getAlgoritmoDeConsenso())
                .fuentes(fuentesOutput)
                .criterioDePertenencia(CriterioOutputDTO.fromDTOtoOutput(dto.getCriterioDePertenencia()))
                .build();
    }
}

