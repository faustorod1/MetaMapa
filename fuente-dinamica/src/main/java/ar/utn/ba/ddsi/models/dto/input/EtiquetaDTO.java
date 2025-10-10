package ar.utn.ba.ddsi.models.dto.input;

import ar.utn.ba.ddsi.models.entities.Etiqueta;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EtiquetaDTO {
    private String nombre;

    public EtiquetaDTO(String nombre) {
        this.nombre = nombre;
    }

    public Etiqueta toEntity() {
        return new Etiqueta(nombre);
    }


}
