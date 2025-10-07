package ar.utn.ba.ddsi.models.dto.output;

import java.util.List;

public class ColeccionOutputDTO {
    private String identificador;
    private String titulo;
    private String descripcion;
    private CriterioOutputDTO criterioDePertenencia;
    private List<FuenteOutputDTO> fuentes;
    private String algoritmoDeConsenso;
}
