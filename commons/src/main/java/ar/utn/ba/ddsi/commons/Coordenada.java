package ar.utn.ba.ddsi.commons;

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

    public static Coordenada fromString(String from) { // Recibe formato "1,2"
        String[] strCoord = from.split(",");
        Double lat = Double.parseDouble(strCoord[0]);
        Double lon = Double.parseDouble(strCoord[1]);
        return new Coordenada(lat, lon);
    }

    public Double[] comoArray(){
        return new Double[]{latitud, longitud};
    }

    public boolean equals(Coordenada other){
        return this.getLatitud().equals(other.getLatitud()) && this.getLongitud().equals(other.getLongitud());
    }
}