package ar.utn.ba.ddsi.models.dtos.inputs;

import ar.utn.ba.ddsi.models.entities.Coleccion;
import ar.utn.ba.ddsi.models.entities.Hecho;
import lombok.Data;

import java.util.List;

@Data
public class ColeccionInputDTO {
    private String identificador;
    private String titulo;
    private String descripcion;
    private List<Hecho> hechos;

    public Coleccion toEntity() {
        return Coleccion.builder()
            .descripcion(this.getDescripcion())
            .titulo(this.getTitulo())
            .identificador(this.getIdentificador())
            .hechos(this.getHechos())
            .build();
    }
}
