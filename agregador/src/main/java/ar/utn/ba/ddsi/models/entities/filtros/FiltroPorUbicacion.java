package ar.utn.ba.ddsi.models.entities.filtros;

import ar.utn.ba.ddsi.models.entities.Hecho;
import jakarta.persistence.*;
import lombok.Data;
import ar.utn.ba.ddsi.commons.Coordenada;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity @DiscriminatorValue("ubicacion")
public class FiltroPorUbicacion extends Filtro {

    @Embedded
    private Coordenada lugar;

    public FiltroPorUbicacion() {}

    public FiltroPorUbicacion(Coordenada lugar) {
        this.lugar = lugar;
    }

    @Override
    public List<Hecho> aplicar(List<Hecho> lista){
        double radio = 50.0; // Si los hechos estan a menos de 1km => se agrega a la lista
        return lista.stream()
            .filter(h -> {
                Coordenada coordHecho = h.getLugarAcontecimiento();
                return coordHecho.estaDentroDeRadio(this.lugar, radio);
            })
            .toList();
    }
}