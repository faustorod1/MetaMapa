package ar.utn.ba.ddsi.models.repositories.impl;

import ar.utn.ba.ddsi.models.entities.Hecho;
import ar.utn.ba.ddsi.models.repositories.IHechosRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class HechosRepository implements IHechosRepository {
    private final List<Hecho> hechos = new ArrayList<Hecho>();

    @Override
    public List<Hecho> findAll(){
        return this.hechos;
    }

    @Override
    public Hecho findById(Long id, Long APIid) {
        return findAll().stream()
                .filter(h -> h.getId().equals(id) && h.getAPIid().equals(APIid))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Hecho> saveAll(List<Hecho> hechosNuevos) {
        for (Hecho hecho : hechosNuevos) {
            Hecho hechoViejo = findById(hecho.getId(), hecho.getAPIid());
            if (hechoViejo != null) {
                actualizarHecho(hechoViejo, hecho);
            }
            else {
                this.hechos.add(hecho);
            }
        }
        return hechos;
    }

    @Override
    public void marcarComoEliminado(Long id,Long APIid){
        Hecho hecho = this.findById(id, APIid);
        if (hecho != null) {
            hecho.setEliminado(true);
        }
    }

    private void actualizarHecho(Hecho hechoAntiguo, Hecho hechoNuevo) {
        hechoAntiguo.setTitulo(hechoNuevo.getTitulo());
        hechoAntiguo.setDescripcion(hechoNuevo.getDescripcion());
        hechoAntiguo.setCategoria(hechoNuevo.getCategoria());
        hechoAntiguo.setContenidoMultimedia(hechoNuevo.getContenidoMultimedia());
        hechoAntiguo.setLugarAcontecimiento(hechoNuevo.getLugarAcontecimiento());
        hechoAntiguo.setFechaHecho(hechoNuevo.getFechaHecho());
        hechoAntiguo.setFechaUltimaActualizacion(hechoNuevo.getFechaUltimaActualizacion());
        hechoAntiguo.setEliminado(hechoNuevo.isEliminado());
    }

}
