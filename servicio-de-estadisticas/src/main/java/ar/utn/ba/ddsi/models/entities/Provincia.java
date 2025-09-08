package ar.utn.ba.ddsi.models.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
public class Provincia {
    private Long id;
    private String nombre;
}
