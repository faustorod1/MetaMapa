package ar.utn.ba.ddsi.models.entities.filtros;

import ar.utn.ba.ddsi.models.entities.Hecho;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity @DiscriminatorColumn(name = "descripcion")
public class FiltroPorDescripcion extends Filtro {
    @Column(name = "descripcion", columnDefinition = "VARCHAR(255)", nullable = false)
    private String descripcion;

    public FiltroPorDescripcion(){}

    public FiltroPorDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public List<Hecho> aplicar(List<Hecho> lista) {
        List<Hecho> filtrados = new ArrayList<>(lista);
        filtrados = lista.stream().filter(h -> h.getDescripcion().contains(descripcion)).toList();

        return filtrados;
    }
}