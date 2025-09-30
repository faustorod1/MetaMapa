package ar.utn.ba.ddsi.models.dtos.output;

import ar.utn.ba.ddsi.models.entities.Categoria;
import lombok.Data;

import java.util.List;

@Data
public class CategoriaDTO {
    private Long id;
    private String nombre;

    public static CategoriaDTO fromEntity(Categoria categoria) {
        CategoriaDTO dto = new CategoriaDTO();
        dto.id = categoria.getId();
        dto.nombre = categoria.getNombre();
        return dto;
    }
}
