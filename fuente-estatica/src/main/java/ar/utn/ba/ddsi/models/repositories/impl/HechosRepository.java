package ar.utn.ba.ddsi.models.repositories.impl;

import ar.utn.ba.ddsi.models.entities.FuenteEstatica;
import ar.utn.ba.ddsi.models.entities.Hecho;
import ar.utn.ba.ddsi.models.repositories.IHechosRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HechosRepository implements IHechosRepository {
    private FuenteEstatica fuente;

    public HechosRepository(){
        this.fuente = new FuenteEstatica("src/test/resources/dataset_prueba.csv");
    }


    @Override
    public Hecho findById(Long id) {
        return this.fuente.getHechos().stream().filter(hecho -> hecho.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public List<Hecho> findAll() {
        return this.fuente.getHechos();
    }
}
