package ar.utn.ba.ddsi.models.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Coordenada {
    private Double latitud;
    private Double longitud;

    public Coordenada(Double latitud, Double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Double[] comoArray(){
        return new Double[]{latitud, longitud};
    }

    public boolean equals(Coordenada other){
        return this.getLatitud() == other.getLatitud() && this.getLongitud() == other.getLongitud();
    }
}