package ar.utn.ba.ddsi.models.entities;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PathDataset {
    Long id;
    String path;
    LocalDateTime fechaCarga;

    public PathDataset(Long id, String path, LocalDateTime fechaCarga) {
        this.id = id;
        this.path = path;
        this.fechaCarga = fechaCarga;
    }
}