package ar.utn.ba.ddsi.commons;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Embeddable
public class Coordenada {
    @Column(name = "latitud")
    private Double latitud;
    @Column(name = "longitud")
    private Double longitud;

    protected Coordenada() {}

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

}