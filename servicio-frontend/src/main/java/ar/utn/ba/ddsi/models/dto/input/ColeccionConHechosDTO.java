package ar.utn.ba.ddsi.models.dto.input;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ColeccionConHechosDTO implements Serializable {
    private String identificador;
    private String titulo;
    private String descripcion;
    private List<HechoDTO> hechos;
    private List<FuenteDTO> fuentes;
}
