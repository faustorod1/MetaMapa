package ar.utn.ba.ddsi.services.impl;

import ar.utn.ba.ddsi.models.entities.CSVReader;
import ar.utn.ba.ddsi.models.entities.Hecho;
import ar.utn.ba.ddsi.models.repositories.IPathsRepository;
import ar.utn.ba.ddsi.services.IPathsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PathsService implements IPathsService {

    @Autowired
    private IPathsRepository pathsRepository;

    @Override
    public List<Hecho> tomarHechos() {      // La idea es que hechosService llame a esta función, y guarde los hechos en hechosRepository.
        return pathsRepository
                .findAll()
                .stream()
                .map(CSVReader::leerHechos)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }


}
