package ar.utn.ba.ddsi.models.entities;

import ar.utn.ba.ddsi.commons.Coordenada;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "hechos")
public class Hecho {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "titulo", columnDefinition = "VARCHAR(100)", nullable = false)
    private String titulo;
    @Column(name = "descripcion", columnDefinition = "TEXT", nullable = false)
    private String descripcion;

    @Column(name = "categoria", columnDefinition = "VARCHAR(100)")
    private String categoria;

    @OneToMany
    @JoinColumn(name = "hecho_id", referencedColumnName = "id")
    private List<ContenidoMultimedia> contenidosMultimedia;

    @Embedded
    private Coordenada lugarAcontecimiento;

    @Column(name = "fecha_del_hecho", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime fechaHecho;

    @Column(name = "fecha_de_carga", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime fechaDeCarga;

    @Column(name = "eliminado", nullable = false)
    private boolean eliminado;          //USO: cuando una solDeElim es aceptada, el hecho se mantiene en el sistema pero no se mostrará en ninguna colección.

    @ManyToOne
    @JoinColumn(name = "contribuyente_id", referencedColumnName = "id", nullable = true)
    private Contribuyente contribuyente;
    @Column(name = "fecha_de_ultima_actualizacion", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime fechaUltimaActualizacion;

    @ManyToOne
    @JoinColumn(name = "solicitud_de_modificacion_id", referencedColumnName = "id")
    @Builder.Default
    private SolicitudDeModificacion solicitudDeModificacion = null;

    @ElementCollection
    @CollectionTable(
            name = "etiquetas_por_hecho",
            joinColumns = @JoinColumn(name = "hecho_id"))
    @Column(name = "etiqueta")
    private List<String> etiquetas = new ArrayList<>();

    @OneToMany
    @JoinColumn(name = "hecho_id", referencedColumnName = "id")
    @Builder.Default
    private List<HechoSnapshot> snapshots = new ArrayList<>();

    protected Hecho() {}

    public void agregarSnapshot(HechoSnapshot snap){
        snapshots.add(snap);
    }

}