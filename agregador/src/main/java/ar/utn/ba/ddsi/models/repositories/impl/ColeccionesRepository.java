package ar.utn.ba.ddsi.models.repositories.impl;

import ar.utn.ba.ddsi.models.entities.Coleccion;
import ar.utn.ba.ddsi.models.entities.Hecho;
import ar.utn.ba.ddsi.models.repositories.IColeccionesRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ColeccionesRepository implements IColeccionesRepository {
    private List<Coleccion> colecciones;

    public List<Coleccion> findAll(){
        return this.colecciones;
    }

    public Coleccion findByIdentificador(String identificador) {
        return colecciones.stream().filter(coleccion -> coleccion.getIdentificador().equals(identificador)).findFirst().orElse(null);
    }

    // TODO: pendiente


}
