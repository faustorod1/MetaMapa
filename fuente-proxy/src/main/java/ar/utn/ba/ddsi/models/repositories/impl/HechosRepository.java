package ar.utn.ba.ddsi.models.repositories.impl;

import ar.utn.ba.ddsi.models.entities.Cache;
import ar.utn.ba.ddsi.models.entities.Hecho;
import ar.utn.ba.ddsi.models.repositories.IHechosRepository;
import lombok.Data;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class HechosRepository implements IHechosRepository {
    private final List<Cache> hechosAPI = new ArrayList<Cache>();
    private final List<Cache> hechosMetamapa = new ArrayList<Cache>();
    private final Integer CacheSize = 17400;
    private final Integer MetaCacheSize = 52200;

    //(Hecho; instanteDeCargaEnCache )

    @Override
    public List<Hecho> findAll() {
        return Stream.concat(hechosMetamapa.stream(), hechosAPI.stream()).map(Cache::getHecho).collect(Collectors.toList());
    }

//-----------------------------------------------------------FINDALL APIsEXTERNAS----------------------------------------------------------
    @Override
    public List<Hecho> findAllAPI(){
        return this.hechosAPI.stream().map(Cache::getHecho).collect(Collectors.toList());
    }

    @Override
    public List<Hecho> findAllAfterAPI(LocalDateTime desde){
        return this.hechosAPI.stream().map(Cache::getHecho).filter(hecho -> hecho.getFechaDeCarga().isAfter(desde)).collect(Collectors.toList());
    }

    @Override
    public List<Hecho> APIsaveAll(List<Hecho> hechosNuevos) {
        for (Hecho hecho : hechosNuevos) {
            Hecho hechoViejo = findById(hecho.getId(), hecho.getAPIid());

            if (hechoViejo != null) {
                actualizarHecho(hechoViejo, hecho);
            } else {
                if (this.hechosAPI.size() >= CacheSize) { // Si alcanzo el tamaño maximo, elimina el más viejo (FIFO)
                    this.hechosAPI.remove(0); // elimina el primero
                }

                this.hechosAPI.add(new Cache(hecho)); // agrega al final
            }
        }
        return hechosAPI.stream().map(Cache::getHecho).collect(Collectors.toList());
    }

//---------------------------------------------------------FINDALL METAMAPA---------------------------------------------------------------
    @Override
    public List<Hecho> findAllMetaMapa(){ return this.hechosMetamapa.stream().map(Cache::getHecho).collect(Collectors.toList());}

    @Override
    public List<Hecho> findAllAfterMetamapa(LocalDateTime desde){
        return this.hechosMetamapa.stream().map(Cache::getHecho).filter(hecho->hecho.getFechaDeCarga().isAfter(desde)).collect(Collectors.toList());
    }

    @Override
    public List<Hecho> metaSaveAll(List<Hecho> hechosNuevos) {
        for (Hecho hecho : hechosNuevos) {
            Hecho hechoViejo = findById(hecho.getId(), hecho.getAPIid());

            if (hechoViejo != null) {
                actualizarHecho(hechoViejo, hecho);
            } else {
                if (this.hechosMetamapa.size() >= MetaCacheSize) { // Si alcanzo el tamaño maximo, elimina el más viejo (FIFO)
                    this.hechosMetamapa.remove(0); // elimina el primero
                }

                this.hechosMetamapa.add(new Cache(hecho)); // agrega al final
            }
        }
        return hechosMetamapa.stream().map(Cache::getHecho).collect(Collectors.toList());
    }

//---------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public Hecho findById(Long id, Long APIid) {
        return findAll().stream()
                .filter(h -> h.getId().equals(id) && h.getAPIid().equals(APIid))
                .findFirst()
                .orElse(null);
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
