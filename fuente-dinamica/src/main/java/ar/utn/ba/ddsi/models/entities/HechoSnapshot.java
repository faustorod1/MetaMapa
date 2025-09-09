package ar.utn.ba.ddsi.models.entities;

import ar.utn.ba.ddsi.commons.Coordenada;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "hechosSnapshots")
public class HechoSnapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titulo", columnDefinition = "VARCHAR(100)", nullable = false)
    private String titulo;

    @Column(name = "descripcion", columnDefinition = "TEXT", nullable = true)
    private String descripcion;

    @Column(name = "categoria", columnDefinition = "VARCHAR(100)", nullable = true)
    private String categoria;

    @OneToMany
    @JoinColumn(name = "contenido_multimedia_id", referencedColumnName = "id")
    private List<ContenidoMultimedia> contenidosMultimedia;

    @Embedded
    private Coordenada lugarAcontecimiento;
    @Column(name = "fecha_del_hecho", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime fechaHecho;
    @Column(name = "fecha_de_carga", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime fechaSnapshot;

    protected HechoSnapshot() {}

    public HechoSnapshot(Hecho hecho) {
        this.titulo = hecho.getTitulo();
        this.descripcion = hecho.getDescripcion();
        this.categoria = hecho.getCategoria();
        this.contenidosMultimedia = hecho.getContenidosMultimedia();
        this.lugarAcontecimiento = hecho.getLugarAcontecimiento();
        this.fechaHecho = hecho.getFechaHecho();
        this.fechaSnapshot = LocalDateTime.now();
    }
}