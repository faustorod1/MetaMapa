package ar.utn.ba.ddsi.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity @Table(name = "usuarios")
public class Usuario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "email", columnDefinition = "VARCHAR(30)", nullable = false)
    private String email;
    @Column(name = "password", columnDefinition = "VARCHAR(255)", nullable = false)
    private String password;
    @Column(name = "nombre", columnDefinition = "VARCHAR(30)", nullable = false)
    private String nombre;
    @Column(name = "apellido", columnDefinition = "VARCHAR(30)", nullable = false)
    private String apellido;
    @Enumerated(EnumType.STRING)
    @Column(name = "rol", columnDefinition = "varchar(50)", nullable = false)
    private Rol rol;
}
