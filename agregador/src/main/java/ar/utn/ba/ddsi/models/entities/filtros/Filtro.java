package ar.utn.ba.ddsi.models.entities.filtros;

import ar.utn.ba.ddsi.models.entities.Hecho;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "filtros")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo")
public abstract class Filtro {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public abstract List<Hecho> aplicar(List<Hecho> lista);
}