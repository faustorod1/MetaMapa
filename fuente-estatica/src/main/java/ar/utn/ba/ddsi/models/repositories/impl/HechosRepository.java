package ar.utn.ba.ddsi.models.repositories.impl;

import ar.utn.ba.ddsi.models.entities.LectorDeCSV;
import ar.utn.ba.ddsi.models.entities.Hecho;
import ar.utn.ba.ddsi.models.entities.PathDataset;
import ar.utn.ba.ddsi.models.repositories.IHechosRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HechosRepository implements IHechosRepository {
    private List<Hecho> hechos;

    @Override
    public List<Hecho> findAllFrom(PathDataset dataset) {
        LectorDeCSV lectorDeCSV = new LectorDeCSV(dataset);
        return lectorDeCSV.getHechos();
    }

}
