package ar.utn.ba.ddsi.models.repositories;

import ar.utn.ba.ddsi.models.entities.PathDataset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IPathsRepository extends JpaRepository<PathDataset, Long> {
    List<PathDataset> findAllByFechaCargaAfter(LocalDateTime fechaCarga);
}
