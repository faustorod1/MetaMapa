package ar.utn.ba.ddsi.models.repositories.impl;

import ar.utn.ba.ddsi.models.entities.PathDataset;
import ar.utn.ba.ddsi.models.repositories.IPathsRepository;
import lombok.Data;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Repository
public class PathsRepository implements IPathsRepository {
    private List<PathDataset> paths;

    public PathsRepository(){
        this.paths = new ArrayList<>();
        this.paths.add(
            new PathDataset(
                1L,
                "fuente-estatica/src/test/resources/dataset_prueba.csv",
                     LocalDate.parse("05/07/2005", DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay()
            )
        );
    }

    @Override
    public List<PathDataset> findAll(){
        return this.paths;
    }

    @Override
    public List<PathDataset> findCargadosDesde(LocalDateTime desde) {
        return this.paths.stream().filter(p -> p.getFechaCarga().isAfter(desde)).collect(Collectors.toList());
    }

    @Override
    public PathDataset save(PathDataset path){
        this.paths.add(path);
        return path;
    }
}
