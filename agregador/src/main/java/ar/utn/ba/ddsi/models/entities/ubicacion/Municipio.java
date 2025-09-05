package ar.utn.ba.ddsi.models.entities.ubicacion;

import lombok.Data;

import java.util.List;

@Data
public class Municipio {
    private Long id;
    private String nombre;
    private Provincia provincia;

}
