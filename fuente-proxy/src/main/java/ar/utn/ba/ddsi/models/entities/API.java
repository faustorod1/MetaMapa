package ar.utn.ba.ddsi.models.entities;

import ar.utn.ba.ddsi.models.entities.APIAdapters.IAPIAdapter;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class API {
    private Long id;
    private String url;
    private boolean metamapa;

    private final IAPIAdapter adapter;

    public API(IAPIAdapter adapter, boolean metamapa) {
        this.adapter = adapter;
        this.metamapa = metamapa;
    }

    public List<Hecho> getAll() {
        List<Hecho> hechos = adapter.getHechos().block();
        hechos.forEach(h -> h.setAPIid(id));
        return hechos;
    }

    public List<Hecho> getAllDesde(LocalDateTime desde){
        return this.getAll().stream().filter(h -> h.getFechaDeCarga().isAfter(desde)).collect(Collectors.toList());
    }
}
