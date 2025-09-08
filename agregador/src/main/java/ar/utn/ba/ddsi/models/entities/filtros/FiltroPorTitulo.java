package ar.utn.ba.ddsi.models.entities.filtros;

import ar.utn.ba.ddsi.models.entities.Hecho;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@Entity @DiscriminatorValue("titulo")
public class FiltroPorTitulo extends Filtro {
    @Column(name = "titulo", columnDefinition = "VARCHAR(100)", nullable = true)
    private String titulo;

    public FiltroPorTitulo() {}

    public FiltroPorTitulo(String titulo) {
        this.titulo = titulo;
    }

    @Override
    public List<Hecho> aplicar(List<Hecho> lista) {
        return lista.stream().filter(h -> h.getTitulo().contains(titulo)).toList();
    }
}