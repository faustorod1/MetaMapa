package ar.utn.ba.ddsi.models.dtos.output;

import ar.utn.ba.ddsi.commons.Coordenada;
import ar.utn.ba.ddsi.models.entities.Hecho;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HechoPreviewDTO {
    Long id;
    String titulo;
    Coordenada lugarAcontecimiento;

    public static HechoPreviewDTO fromEntity(Hecho hecho) {
        HechoPreviewDTO dto = new HechoPreviewDTO();
        dto.setId(hecho.getId());
        dto.setTitulo(hecho.getTitulo());
        dto.setLugarAcontecimiento(hecho.getLugarAcontecimiento());
        return dto;
    }
}
