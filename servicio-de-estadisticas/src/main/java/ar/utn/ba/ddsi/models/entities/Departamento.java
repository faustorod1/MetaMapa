package ar.utn.ba.ddsi.models.entities;

import lombok.Data;

@Data
public class Departamento {
    private Long id;
    private String nombre;
    private Provincia provincia;
}
