package ar.utn.ba.ddsi.models.repositories.impl;

import ar.utn.ba.ddsi.commons.CSVReader;
import ar.utn.ba.ddsi.models.entities.Hecho;
import ar.utn.ba.ddsi.models.repositories.IHechosRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class HechosRepository implements IHechosRepository {
    private List<Hecho> hechos;


    public HechosRepository(){
        this.hechos = CSVReader.leer(pathArchivo);
    }

    @Override
    public Hecho findById(Long id) {
        return this.fuente.getHechos().stream().filter(hecho -> hecho.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public List<Hecho> findAll() {
        return this.fuente.getHechos();
    }

    @Override
    public void addAll(List<Hecho> hechosNuevos){//TODO: esto quizas deberia tener otro nombre
        this.hechos = hechosNuevos;
    }
}
