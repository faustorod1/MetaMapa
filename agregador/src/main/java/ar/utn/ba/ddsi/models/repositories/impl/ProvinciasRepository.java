package ar.utn.ba.ddsi.models.repositories.impl;

import ar.utn.ba.ddsi.models.entities.ubicacion.Provincia;
import ar.utn.ba.ddsi.models.repositories.IProvinciasRepository;

import java.util.List;

public class ProvinciasRepository implements IProvinciasRepository {
    private List<Provincia> provincias;
    private Long idActual = 0L;

    public ProvinciasRepository() {
    }

    public List<Provincia> findAll(){
        return this.provincias;
    }

    public Provincia findById(Long id) {
        return this.provincias.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
    }

    public void save(Provincia provincia){
        Provincia provinciaVieja = findById(provincia.getId());
        if (provinciaVieja == null){
            provincia.setId(idActual);
            idActual++;
            this.provincias.add(provincia);
        } else {
            update(provinciaVieja, provincia);
        }
    }

    private void update(Provincia provinciaVieja, Provincia provinciaNueva) {
        provinciaNueva.setId(provinciaVieja.getId());
        provinciaNueva.setNombre(provinciaVieja.getNombre());
    }

    public void delete(Long id){
        Provincia provincia = this.findById(id);
        this.provincias.remove(provincia);
    }
}
