package ar.utn.ba.ddsi.models.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Categoria {
    private String nombre;

    public Categoria(String nombre) {
        this.nombre = nombre;
    }
}