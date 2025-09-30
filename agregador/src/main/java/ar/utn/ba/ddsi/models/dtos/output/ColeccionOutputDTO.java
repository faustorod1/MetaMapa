package ar.utn.ba.ddsi.models.dtos.output;

import ar.utn.ba.ddsi.models.dtos.input.FuenteDTO;
import ar.utn.ba.ddsi.models.entities.Coleccion;
import ar.utn.ba.ddsi.models.entities.Criterio;
import ar.utn.ba.ddsi.models.entities.Fuente;
import ar.utn.ba.ddsi.models.entities.Hecho;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ColeccionOutputDTO {
    private String identificador;
    private String titulo;
    private String descripcion;
    private CriterioOutputDTO criterioDePertenencia;
    private List<FuenteDTO> fuentes;

    public static ColeccionOutputDTO fromEntity(Coleccion coleccion) {
        ColeccionOutputDTO dto = new ColeccionOutputDTO();

        dto.setIdentificador(coleccion.getIdentificador());
        dto.setTitulo(coleccion.getTitulo());
        dto.setDescripcion(coleccion.getDescripcion());
        dto.setFuentes(coleccion.getFuentes().stream().map(FuenteDTO::fromEntity).toList());
        dto.setCriterioDePertenencia(CriterioOutputDTO.fromEntity(coleccion.getCriterioDePertenencia()));
        return dto;
    }
}
