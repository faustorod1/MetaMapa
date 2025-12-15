package ar.utn.ba.ddsi.models.entities;

import ar.utn.ba.ddsi.commons.Coordenada;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import ar.utn.ba.ddsi.models.entities.ContenidoMultimedia;

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


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "hecho_id")
    private List<ContenidoMultimedia> contenidosMultimedia = new ArrayList<>();

    @Embedded
    private Coordenada lugarAcontecimiento;

    @Column(name = "fecha_del_hecho", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime fechaHecho;

    @Column(name = "fecha_de_carga", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime fechaDeCarga;

    @Column(name = "eliminado", nullable = false)
    private boolean eliminado;          //USO: cuando una solDeElim es aceptada, el hecho se mantiene en el sistema pero no se mostrará en ninguna colección.

    @Column(name = "contribuyenteId", nullable = true)
    private Long contribuyenteId;

    @Column(name = "fecha_de_ultima_actualizacion", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime fechaUltimaActualizacion;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private SolicitudDeModificacion solicitudDeModificacion = null;

    @ElementCollection
    @CollectionTable(
            name = "etiquetas",
            joinColumns = @JoinColumn(name = "hecho_id"))
    @Column(name = "etiqueta")
    private Set<String> etiquetas = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "hecho_id", referencedColumnName = "id")
    @Builder.Default
    private List<HechoSnapshot> snapshots = new ArrayList<>();

    protected Hecho() {}

    public void agregarSnapshot(HechoSnapshot snap){
        snapshots.add(snap);
    }

    public List<String> todoMultimediaString(){
        List<String> todos = new ArrayList<>();
        for (ContenidoMultimedia multimedia : this.contenidosMultimedia){
            todos.add(contenidosMultimedia.toString());
        }
        return todos;
    }
}