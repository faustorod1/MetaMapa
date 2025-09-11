package ar.utn.ba.ddsi.models.dtos.input;

import ar.utn.ba.ddsi.converters.AlgoritmoDeConsensoConverter;
import ar.utn.ba.ddsi.models.entities.Coleccion;
import lombok.Data;

import java.util.List;

@Data
public class ColeccionInputDTO {
    private String identificador;
    private String titulo;
    private String descripcion;
    private CriterioInputDTO criterioDePertenencia;
    private List<String> fuentes;
    private String algoritmoDeConsenso;

    public Coleccion toEntity(){
        return new Coleccion(
                this.getIdentificador(),
                this.getTitulo(),
                this.getDescripcion(),
                this.getCriterioDePertenencia().toEntity(),
                this.getFuentes(),
                new AlgoritmoDeConsensoConverter().convertToEntityAttribute(this.getAlgoritmoDeConsenso())
        );
    }
}
