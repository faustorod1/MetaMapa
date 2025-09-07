package ar.utn.ba.ddsi.models.entities.filtros;

import ar.utn.ba.ddsi.models.entities.Hecho;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.util.List;

@Entity @DiscriminatorValue("eliminados")
public class FiltroPorEliminados extends Filtro {

    @Override
    public List<Hecho> aplicar(List<Hecho> lista) {
        return lista.stream().filter(h -> !h.isEliminado()).toList();
    }
}