package ar.utn.ba.ddsi.models.repositories.impl;

import ar.utn.ba.ddsi.models.entities.Coleccion;
import ar.utn.ba.ddsi.models.entities.Hecho;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class ColeccionesRepository {
    private List<Coleccion> colecciones;

    public List<Coleccion> findall(){
        return this.colecciones;
    }

    // TODO: pendiente


}
