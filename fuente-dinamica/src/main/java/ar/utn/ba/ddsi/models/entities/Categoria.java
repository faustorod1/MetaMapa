package ar.utn.ba.ddsi.models.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "categorias")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", columnDefinition = "VARCHAR(30)", nullable = false)
    private String nombre;

    protected Categoria() {}

    public Categoria(String nombre) {
        this.nombre = nombre;
    }
}