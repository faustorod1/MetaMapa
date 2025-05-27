package ar.utn.ba.ddsi.models.repositories.impl;

import ar.utn.ba.ddsi.models.entities.Hecho;
import ar.utn.ba.ddsi.models.repositories.IHechosRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@Repository
public class HechosRepository implements IHechosRepository {

    private List<Hecho> hechos = new ArrayList<Hecho>();
    private Long idActual = 0L;

    @Override
    public List<Hecho> findAll(){
        return this.hechos;
    }


    @Override
    public List<Hecho> saveAll(List<Hecho> hechosNuevos) {
        for (Hecho hecho : hechosNuevos) {
            // TODO: Revisar si ya está en la lista, y en ese caso solo actualizarlo
            idActual++;
            hecho.setId(idActual);
            this.hechos.add(hecho);
        }
        return hechos;
    }



    @Override
    public void deleteAll(){
        this.hechos.clear();
    }
}
