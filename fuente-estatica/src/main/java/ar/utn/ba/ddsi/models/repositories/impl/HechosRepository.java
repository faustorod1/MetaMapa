package ar.utn.ba.ddsi.models.repositories.impl;

import ar.utn.ba.ddsi.models.entities.FuenteEstatica;
import ar.utn.ba.ddsi.models.entities.Hecho;
import ar.utn.ba.ddsi.models.entities.PathDataset;
import ar.utn.ba.ddsi.models.repositories.IHechosRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HechosRepository implements IHechosRepository {

    @Override
    public List<Hecho> findAllFrom(PathDataset dataset) {
        FuenteEstatica fuente = new FuenteEstatica(dataset.getPath());
        return fuente.getHechos();
    }

}
