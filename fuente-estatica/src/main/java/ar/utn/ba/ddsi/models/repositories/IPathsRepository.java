package ar.utn.ba.ddsi.models.repositories;

import ar.utn.ba.ddsi.models.entities.PathDataset;

import java.time.LocalDateTime;
import java.util.List;

public interface IPathsRepository {
    List<PathDataset> findAll();
    List<PathDataset> findCargadosDesde(LocalDateTime desde);
    PathDataset save(PathDataset path);

}
