package ar.utn.ba.ddsi.models.entities.filtros;

import ar.utn.ba.ddsi.models.entities.Hecho;
import jakarta.persistence.Column;
import java.text.Normalizer;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;

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
        String tituloBusqueda = normalizar(this.titulo);

        return lista.stream().filter(h -> {
            String tituloHecho = normalizar(h.getTitulo());
            return tituloHecho.contains(tituloBusqueda);
        }).toList();
    }

    private String normalizar(String input) {
        if (input == null) return "";

        String normalizado = Normalizer.normalize(input, Normalizer.Form.NFD);
        return normalizado.replaceAll("\\p{M}", "").toLowerCase();
    }
}