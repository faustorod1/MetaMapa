package ar.edu.utn.frba.dds;

import lombok.Data;

@Data
public class Categoria {
    private String nombre;

    public Categoria(String nombre) {
        this.nombre = nombre;
    }
}