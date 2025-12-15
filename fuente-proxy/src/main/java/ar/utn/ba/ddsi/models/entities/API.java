package ar.utn.ba.ddsi.models.entities;

import ar.utn.ba.ddsi.models.entities.APIAdapters.APIAdapter;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class API {
    private Long id;
    private String url;
    private LocalDateTime fechaUltimaActualizacion = LocalDateTime.parse("1000-01-01T00:00:00");
    private boolean metamapa;

    private final APIAdapter adapter;

    public API(APIAdapter adapter, boolean metamapa) {
        this.adapter = adapter;
        this.metamapa = metamapa;
    }

    public List<Hecho> getAll() {
        List<Hecho> hechos = adapter.getHechos().block();
        hechos.forEach(h -> h.setAPIid(id));
        return hechos;
    }

    public List<Hecho> getAllDesde(LocalDateTime desde){
        List<Hecho> hechos = adapter.getHechosDesde(desde);
        if (hechos == null || hechos.isEmpty()) {
            return new ArrayList<>();
        }

        hechos.forEach(h -> h.setAPIid(id));
        return hechos;
    }

    public Hecho getById(String id) {
        return adapter.getById(id);
    }

    public List<Hecho> getNuevos() {
        return getAllDesde(fechaUltimaActualizacion);
    }
}
