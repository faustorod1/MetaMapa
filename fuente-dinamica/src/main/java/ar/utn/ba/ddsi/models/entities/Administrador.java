package ar.utn.ba.ddsi.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "administradores")
public class Administrador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", columnDefinition = "VARCHAR(30)", nullable = false)
    private String nombre;

    @Column(name = "apellido", columnDefinition = "VARCHAR(50)", nullable = false)
    private String apellido;
}