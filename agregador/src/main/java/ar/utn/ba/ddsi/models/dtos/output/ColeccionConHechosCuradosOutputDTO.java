package ar.utn.ba.ddsi.models.dtos.output;

import ar.utn.ba.ddsi.models.dtos.input.FuenteDTO;


import java.util.List;
import lombok.Data;

@Data
public class ColeccionConHechosCuradosOutputDTO {
    private String identificador;
    private String titulo;
    private String descripcion;
    private List<HechoOutputDTO> hechos;
    private List<FuenteDTO> fuentes;
}
