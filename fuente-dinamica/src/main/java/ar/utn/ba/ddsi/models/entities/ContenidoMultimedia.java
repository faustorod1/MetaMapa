package ar.utn.ba.ddsi.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "contenidos_multimedia")
@Data
@NoArgsConstructor
public class ContenidoMultimedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "path_imagen", columnDefinition = "VARCHAR(300)", nullable = false)
    private String url;


    public ContenidoMultimedia(String url){
        this.url = url;
    }
}