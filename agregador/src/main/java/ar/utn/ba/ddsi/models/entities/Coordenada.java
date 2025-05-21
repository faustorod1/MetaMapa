package ar.utn.ba.ddsi.models.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Coordenada {
    private double latitud;
    private double longitud;

    public Coordenada(double latitud, double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Coordenada(double[] array) {
        this.latitud = array[0];
        this.longitud = array[1];
    }

    public double[] comoArray(){
        return new double[]{latitud, longitud};
    }

    public boolean equals(Coordenada other){
        return this.getLatitud() == other.getLatitud() && this.getLongitud() == other.getLongitud();
    }
}