package ar.utn.ba.ddsi.models.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
public class Municipio {
    private Long id;
    private String nombre;
    private Provincia provincia;
}
