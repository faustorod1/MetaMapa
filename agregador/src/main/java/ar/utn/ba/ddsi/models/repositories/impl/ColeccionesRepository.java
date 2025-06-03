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
    private List<HechoXColeccion> hechosXColecciones;

    public ColeccionesRepository() {
        // TODO Después sacar esto, no deberían estar hardcodeadas
        colecciones = new ArrayList<>();
        hechosXColecciones = new ArrayList<>();

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


    // Hechos X Colección

    public void setRelacionHechoXColeccion(String identificadorColeccion, Long hechoId, boolean presente) {
        if (presente) {
            agregarHechoAColeccion(identificadorColeccion, hechoId);
        } else {
            eliminarHechoDeColeccion(identificadorColeccion, hechoId);
        }
    }

    public List<Long> getHechosIdXColeccion(String identificadorColeccion) {
        return hechosXColecciones.stream()
            .filter(hxc -> hxc.getColeccionIdentificador().equals(identificadorColeccion))
            .map(HechoXColeccion::getHechoId)
            .toList();
    }

    public List<String> getIdentificadoresColeccionXHecho(Long hechoId) {
        return hechosXColecciones.stream()
            .filter(hxc -> hxc.getHechoId().equals(hechoId))
            .map(HechoXColeccion::getColeccionIdentificador)
            .toList();
    }


    public HechoXColeccion findHechoXColeccion(String identificadorColeccion, Long hechoId) {
        return hechosXColecciones.stream().filter(hxc ->
            hxc.getColeccionIdentificador().equals(identificadorColeccion) && hxc.getHechoId().equals(hechoId))
            .findFirst().orElse(null);
    }

    public void agregarHechoAColeccion(String identificadorColeccion, Long hechoId) {
        if (findHechoXColeccion(identificadorColeccion, hechoId) != null) {
            return;
        }
        HechoXColeccion hxc = new HechoXColeccion(identificadorColeccion, hechoId);
        hechosXColecciones.add(hxc);
    }

    public boolean eliminarHechoDeColeccion(String identificadorColeccion, Long hechoId) {
        HechoXColeccion hxc = findHechoXColeccion(identificadorColeccion, hechoId);
        if (hxc != null) {
            return hechosXColecciones.remove(hxc);
        }
        return false;
    }

    public boolean isHechoInColeccion(String identificadorColeccion, Long hechoId) {
        return findHechoXColeccion(identificadorColeccion, hechoId) != null;
    }
}
