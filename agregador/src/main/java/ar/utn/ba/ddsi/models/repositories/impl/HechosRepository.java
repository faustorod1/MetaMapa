package ar.utn.ba.ddsi.models.repositories.impl;

import ar.utn.ba.ddsi.models.entities.Hecho;
import ar.utn.ba.ddsi.models.repositories.IHechosRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class HechosRepository implements IHechosRepository {
    private List<Hecho> hechos = new ArrayList<Hecho>();

    public List<Hecho> findAll(){
        return this.hechos;
    }
}
