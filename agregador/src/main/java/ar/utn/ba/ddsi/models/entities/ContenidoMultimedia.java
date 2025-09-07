package ar.utn.ba.ddsi.models.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "contenidos_multimedia")
public class ContenidoMultimedia {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "path_imagen", columnDefinition = "VARCHAR(255)", nullable = false)
    private String pathImagen;
}