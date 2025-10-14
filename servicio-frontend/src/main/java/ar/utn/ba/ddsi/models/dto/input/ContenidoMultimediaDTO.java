package ar.utn.ba.ddsi.models.dto.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContenidoMultimediaDTO {
    Long id;
    private String pathImagen;

}
