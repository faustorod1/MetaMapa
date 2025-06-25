package ar.utn.ba.ddsi.models.repositories.impl;

import ar.utn.ba.ddsi.models.entities.Hecho;
import ar.utn.ba.ddsi.models.repositories.IHechosRepository;
import org.springframework.stereotype.Repository;

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
    public Hecho findById(Long id) {
        return findAll().stream().filter(h -> h.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public List<Hecho> findFromFuente(String fuente) {
        return findAll().stream().filter(h -> h.perteneceALaFuente(fuente)).toList();
    }

    @Override
    public List<Hecho> findFromFuentes(List<String> fuentes) {
        return findAll().stream().filter(h ->
            fuentes.stream().anyMatch(h::perteneceALaFuente)
        ).toList();
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
