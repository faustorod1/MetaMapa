package ar.utn.ba.ddsi.models.repositories.impl;

import ar.utn.ba.ddsi.models.entities.Categoria;
import ar.utn.ba.ddsi.models.entities.Coleccion;
import ar.utn.ba.ddsi.models.entities.Criterio;
import ar.utn.ba.ddsi.models.entities.FiltroPorCategoria;
import ar.utn.ba.ddsi.models.entities.FiltroPorFechaHecho;
import ar.utn.ba.ddsi.models.entities.Hecho;
import ar.utn.ba.ddsi.models.entities.HechoXColeccion;
import ar.utn.ba.ddsi.models.repositories.IColeccionesRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ColeccionesRepository implements IColeccionesRepository {
    private List<Coleccion> colecciones;

    public ColeccionesRepository() {
        // TODO Después sacar esto, no deberían estar hardcodeadas
        colecciones = new ArrayList<>();

        Criterio crit1 = Criterio.nuevo()
            .addFiltro(new FiltroPorCategoria(new Categoria("Copiosa caída de nieve")));
        Criterio crit2 = Criterio.nuevo()
            .addFiltro(new FiltroPorFechaHecho(null, "31/12/2010"));

        colecciones.add(
            new Coleccion("1", "Cosas con caída de nieve", "Hechos cuya categoría es 'Copiosa caída de nieve'.", crit1)
        );
        colecciones.add(
            new Coleccion("2", "Hechos viejos", "Hechos anteriores al 2011", crit2)
        );
    }

    public List<Coleccion> findAll(){
        return this.colecciones;
    }

    public Coleccion findByIdentificador(String identificador) {
        return colecciones.stream().filter(coleccion -> coleccion.getIdentificador().equals(identificador)).findFirst().orElse(null);
    }

    public void save(Coleccion coleccion){
        this.colecciones.add(coleccion);
    }
}
