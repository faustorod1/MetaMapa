package ar.utn.ba.ddsi.models.entities;

import jakarta.persistence.*;

@Entity
public class ContenidoMultimedia {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "url", nullable = false)
    private String url;
}