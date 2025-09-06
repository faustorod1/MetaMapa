package ar.utn.ba.ddsi.models.entities.filtros;

import ar.utn.ba.ddsi.models.entities.Categoria;
import ar.utn.ba.ddsi.models.entities.Hecho;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity @DiscriminatorColumn(name = "categoria")
public class FiltroPorCategoria extends Filtro{
    @ManyToOne
    @JoinColumn(name = "categoria_id", referencedColumnName = "id", nullable = false)
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