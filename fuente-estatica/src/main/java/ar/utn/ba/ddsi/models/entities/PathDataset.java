package ar.utn.ba.ddsi.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pathsDatasets")
@Data
public class PathDataset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name="path", columnDefinition = "TEXT", nullable = false)
    String path;

    @Column(name="fecha_carga", nullable = false)
    LocalDateTime fechaCarga;

    protected PathDataset() {}

    public PathDataset(String path, LocalDateTime fechaCarga) {
        this.path = path;
        this.fechaCarga = fechaCarga;
    }

    public PathDataset(Long id, String path, LocalDateTime fechaCarga) {
        this.id = id;
        this.path = path;
        this.fechaCarga = fechaCarga;
    }

    public List<Hecho> cargarHechos() {
        LectorDeCSV reader = new LectorDeCSV(this);
        return reader.getHechos();
    }
}