package ar.utn.ba.ddsi.models.dto.input;

import ar.utn.ba.ddsi.models.entities.Etiqueta;
import lombok.Data;

@Data
public class EtiquetaDTO {
    private String nombre;

    public Etiqueta toEntity() {
        return new Etiqueta(nombre);
    }

    public static EtiquetaDTO fromEntity(Etiqueta etiqueta) {
        EtiquetaDTO dto = new EtiquetaDTO();
        dto.nombre = etiqueta.getNombre();
        return dto;
    }
}
