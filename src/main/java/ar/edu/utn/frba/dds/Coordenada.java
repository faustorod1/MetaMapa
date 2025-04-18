package ar.edu.utn.frba.dds;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Coordenada {
    private float latitud;
    private float longitud;

    public Coordenada(float latitud, float longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public float[] comoArray(){
        return new float[]{latitud, longitud};
    }

    public boolean equals(Coordenada other){
        return this.getLatitud() == other.getLatitud() && this.getLongitud() == other.getLongitud();
    }
}