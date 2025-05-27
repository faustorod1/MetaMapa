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
            Hecho hechoViejo = findByIdExterno(hecho.getIdExterno());
            if (hechoViejo != null) {
                actualizarHecho(hechoViejo, hecho);
            }
            else {
                idActual++;
                hecho.setId(idActual);
                this.hechos.add(hecho);
            }
        }
        return hechos;
    }

    @Override
    public void deleteAll(){
        this.hechos.clear();
    }

    public Hecho findByIdExterno(String idExterno) {
        return hechos.stream().filter(h -> h.getIdExterno().equals(idExterno)).findFirst().orElse(null);
    }

    private void actualizarHecho(Hecho hechoAntiguo, Hecho hechoNuevo) {
        hechoAntiguo.setTitulo(hechoNuevo.getTitulo());
        hechoAntiguo.setDescripcion(hechoNuevo.getDescripcion());
        hechoAntiguo.setCategoria(hechoNuevo.getCategoria());
        hechoAntiguo.setContenidoMultimedia(hechoNuevo.getContenidoMultimedia());
        hechoAntiguo.setOrigen(hechoNuevo.getOrigen());
        hechoAntiguo.setLugarAcontecimiento(hechoNuevo.getLugarAcontecimiento());
        hechoAntiguo.setFechaHecho(hechoNuevo.getFechaHecho());
        hechoAntiguo.setFechaUltimaActualizacion(hechoNuevo.getFechaUltimaActualizacion());
        hechoAntiguo.setEliminado(hechoNuevo.isEliminado());
        hechoAntiguo.setContribuyente(hechoNuevo.getContribuyente());
        hechoAntiguo.setRevisado(hechoAntiguo.isRevisado());
        hechoAntiguo.setSolicitudesDeEliminacion(hechoNuevo.getSolicitudesDeEliminacion());
        hechoAntiguo.setEtiquetas(hechoNuevo.getEtiquetas());
    }
}
