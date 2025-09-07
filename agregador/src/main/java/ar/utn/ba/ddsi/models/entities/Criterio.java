package ar.utn.ba.ddsi.models.entities;

import ar.utn.ba.ddsi.commons.Coordenada;
import ar.utn.ba.ddsi.models.entities.filtros.*;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter

@Entity
@Table(name = "criterios")
public class Criterio {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    @JoinColumn(name="criterio_id", referencedColumnName = "id", nullable=false)
    private List<Filtro> filtros = new ArrayList<>();

    public Criterio(){
        filtros.add(new FiltroPorEliminados());
    }

    public Criterio(Map<String, String> params){
        filtros.add(new FiltroPorEliminados());
        params.forEach((key, val) -> {
        switch (key) {
                case "categoria":
                    this.addFiltro(new FiltroPorCategoria((new Categoria(val))));
                    break;
               case "fecha_reporte_desde":
                    this.addFiltro(FiltroPorFechaDeCarga.FiltrarDesde(val));
                    break;
                case "fecha_reporte_hasta":
                    this.addFiltro(FiltroPorFechaDeCarga.FiltrarHasta(val));
                    break;
                case "fecha_acontecimiento_desde":
                    this.addFiltro(FiltroPorFechaHecho.FiltrarDesde(val));
                    break;
                case "fecha_acontecimiento_hasta":
                    this.addFiltro(FiltroPorFechaHecho.FiltrarHasta(val));
                    break;
                case "ubicacion":
                    this.addFiltro(new FiltroPorUbicacion(Coordenada.fromString(val)));
                    break;
                default:
                    break;
            }
        });
    }

    public static Criterio nuevo() {
        return new Criterio();
    }//TODO remover si quedo viejo

    public Criterio addFiltro(Filtro filtro) {
        filtros.add(filtro);
        return this;
    }

    public List<Hecho> aplicarA(List<Hecho> listaOriginal){
        List<Hecho> hechos = new ArrayList<>(listaOriginal);

        for (Filtro filtro : filtros) {
            hechos = filtro.aplicar(hechos);
        }
        return hechos;
    }

}