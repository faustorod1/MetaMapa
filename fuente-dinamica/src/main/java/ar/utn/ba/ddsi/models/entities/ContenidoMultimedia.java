package ar.utn.ba.ddsi.models.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "contenido_multimedia")
public class ContenidoMultimedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "path_imagen", columnDefinition = "VARCHAR(300)", nullable = false)
    private String path;

    public ContenidoMultimedia(String path){
        this.path = path;
    }

    protected ContenidoMultimedia() {}
}