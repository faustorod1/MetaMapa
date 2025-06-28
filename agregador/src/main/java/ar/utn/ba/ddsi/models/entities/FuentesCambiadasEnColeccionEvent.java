package ar.utn.ba.ddsi.models.entities;

import lombok.Getter;

import java.util.List;

public class FuentesCambiadasEnColeccionEvent {
    @Getter
    public Coleccion coleccion;
    @Getter
    public List<String> fuentesCambiadas;

    public FuentesCambiadasEnColeccionEvent(Coleccion coleccion, List<String> fuentesCambiadas) {
        this.coleccion = coleccion;
        this.fuentesCambiadas = fuentesCambiadas;
    }

}
