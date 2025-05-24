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

    @Override
    public List<Hecho> findAll(){
        return this.hechos;
    }


    @Override
    public List<Hecho> saveAll(List<Hecho> hechosNuevos) {
        hechos.addAll(hechosNuevos);
        return hechos;

        /*
        for (Hecho hecho : hechosNuevos) {
        //TODO comprobar si el hecho no se modifico => no hace falta actualizarlo
        hecho

        }
        */
    }



    @Override
    public void deleteAll(){
        this.hechos.clear();
    }
}
