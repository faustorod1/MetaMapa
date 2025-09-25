package ar.utn.ba.ddsi.models.entities;

import lombok.Getter;

import java.util.List;

public class FuentesCambiadasEnColeccionEvent {
    @Getter
    public Coleccion coleccion;
    @Getter
    public List<Fuente> fuentesCambiadas;

    public FuentesCambiadasEnColeccionEvent(Coleccion coleccion, List<Fuente> fuentesCambiadas) {
        this.coleccion = coleccion;
        this.fuentesCambiadas = fuentesCambiadas;
    }

}
