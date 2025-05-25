package ar.utn.ba.ddsi.services;

import ar.utn.ba.ddsi.models.entities.PathDataset;

import java.time.LocalDateTime;
import java.util.List;


public interface IPathsService {
    List<PathDataset> obtenerPathsDesde(LocalDateTime desde);
}
