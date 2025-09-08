package ar.utn.ba.ddsi.models.entities.filtros;

import ar.utn.ba.ddsi.models.entities.Categoria;
import ar.utn.ba.ddsi.models.entities.Hecho;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity @DiscriminatorValue("categoria")
public class FiltroPorCategoria extends Filtro{
    @ManyToOne
    @JoinColumn(name = "categoria_id", referencedColumnName = "id", nullable = true)
    private Categoria categoria;

    protected FiltroPorCategoria() {}

    public FiltroPorCategoria(Categoria categoria){
        this.categoria = categoria;
    }

    @Override
    public List<Hecho> aplicar(List<Hecho> hechos){
        return hechos.stream().filter(hecho -> hecho.getCategoria().equals(categoria)).collect(Collectors.toList());
    }
}