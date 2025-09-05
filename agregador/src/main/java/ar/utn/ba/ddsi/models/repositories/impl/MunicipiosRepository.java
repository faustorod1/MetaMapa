package ar.utn.ba.ddsi.models.repositories.impl;

import ar.utn.ba.ddsi.models.entities.ubicacion.Municipio;
import ar.utn.ba.ddsi.models.repositories.IMunicipiosRepository;

import java.util.List;

public class MunicipiosRepository implements IMunicipiosRepository {
    private List<Municipio> municipios;
    private Long idActual = 0L;

    public MunicipiosRepository() {
    }

    public List<Municipio> findAll(){
        return this.municipios;
    }

    public Municipio findById(Long id) {
        return this.municipios.stream().filter(m -> m.getId().equals(id)).findFirst().orElse(null);
    }

    public void save(Municipio municipio){
        Municipio municipioViejo = findById(municipio.getId());
        if (municipioViejo == null){
            municipio.setId(idActual);
            idActual++;
            this.municipios.add(municipio);
        } else {
            update(municipioViejo, municipio);
        }
    }

    private void update(Municipio municipioViejo, Municipio municipioNuevo) {
        municipioNuevo.setId(municipioViejo.getId());
        municipioNuevo.setNombre(municipioViejo.getNombre());
        municipioNuevo.setProvincia(municipioViejo.getProvincia());
    }

    public void delete(Long id){
        Municipio municipio = this.findById(id);
        this.municipios.remove(municipio);
    }
}
