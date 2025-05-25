package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.models.entities.PathDataset;
import ar.utn.ba.ddsi.models.repositories.IPathsRepository;
import ar.utn.ba.ddsi.services.IPathsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PathsService implements IPathsService {

    @Autowired
    private IPathsRepository pathsRepository;

    @Override
    public List<PathDataset> obtenerPathsDesde(LocalDateTime desde) {
        return pathsRepository.findCargadosDesde(desde);
    }


}
