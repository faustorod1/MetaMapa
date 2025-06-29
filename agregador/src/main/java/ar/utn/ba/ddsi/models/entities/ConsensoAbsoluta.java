package ar.utn.ba.ddsi.models.entities;

import ar.utn.ba.ddsi.models.repositories.IHechosRepository;

import java.util.List;

public class ConsensoAbsoluta implements IAlgoritmoDeConsenso {

    @Override
    public List<Hecho> consensuar(List<Hecho> hechosColeccion, List<String> fuentesColeccion) {
        return hechosColeccion
                .stream()
                .filter(hecho -> {
                    String titulo = hecho.getTitulo();
                    return fuentesColeccion
                            .stream()
                            .allMatch(fuente -> hechosColeccion.stream().anyMatch(
                                    h -> h.getTitulo().equals(titulo)
                                    && h.perteneceALaFuente(fuente)));
                })
                .toList();
    }
}
