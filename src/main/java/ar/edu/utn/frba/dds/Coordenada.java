package ar.edu.utn.frba.dds;

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

    public double[] comoArray(){
        return new double[]{latitud, longitud};
    }

    public boolean equals(Coordenada other){
        return this.getLatitud() == other.getLatitud() && this.getLongitud() == other.getLongitud();
    }
}