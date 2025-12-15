package ar.utn.ba.ddsi.models.entities;

import ar.utn.ba.ddsi.commons.Coordenada;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity @Table(name = "hechos")
public class Hecho {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "id_externo", nullable = false)
    private String idExterno;
    @Column(name = "api_id")
    private Long APIid;
    @Column(name = "titulo")
    private String titulo;
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;
    @Column(name = "categoria")
    private String categoria;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "hecho_id")
    private List<ContenidoMultimedia> contenidoMultimedia;
    @Embedded
    private Coordenada lugarAcontecimiento;
    @Column(name = "fecha_hecho")
    private LocalDateTime fechaHecho;
    @Column(name = "fecha_de_carga")
    private LocalDateTime fechaDeCarga;
    @Column(name = "fecha_ultima_actualizacion")
    private LocalDateTime fechaUltimaActualizacion;
    @Column(name = "eliminado")
    private boolean eliminado;
    @ElementCollection
    @CollectionTable(
            name = "etiquetas",
            joinColumns = @JoinColumn(name = "hecho_id"))
    @Column(name = "etiqueta")
    private Set<String> etiquetas = new HashSet<>();
}