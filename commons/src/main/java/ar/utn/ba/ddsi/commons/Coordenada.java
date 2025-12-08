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

    public boolean estaDentroDeRadio(Coordenada otra, double radioKm) {
        if (otra == null || this.latitud == null || this.longitud == null ||
                otra.latitud == null || otra.longitud == null) {
            return false;
        }

        // Fórmula del Haversine

        final int RADIO_TIERRA_KM = 6371; // Radio aproximado de la Tierra

        double dLat = Math.toRadians(otra.latitud - this.latitud);
        double dLon = Math.toRadians(otra.longitud - this.longitud);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(this.latitud)) * Math.cos(Math.toRadians(otra.latitud)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distanciaReal = RADIO_TIERRA_KM * c;

        return distanciaReal <= radioKm;
    }

}