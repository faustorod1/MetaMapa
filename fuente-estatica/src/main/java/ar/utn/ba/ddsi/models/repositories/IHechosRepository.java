package ar.utn.ba.ddsi.models.repositories;

import java.util.List;
import ar.utn.ba.ddsi.models.entities.Hecho;
import ar.utn.ba.ddsi.models.entities.PathDataset;

public interface IHechosRepository {
    List<Hecho> findAllFrom(PathDataset dataset);
}
