package ar.utn.ba.ddsi.models.repositories.impl;

import ar.utn.ba.ddsi.models.entities.Hecho;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class HechosRepository {
    private List<Hecho> hechos;

    public List<Hecho> findall(){
        return this.hechos;
    }


}
