package ar.utn.ba.ddsi.models.entities;

import ar.utn.ba.ddsi.commons.Coordenada;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public class Criterio implements ICriterioInmutable {
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
                    this.addFiltro(new FiltroPorUbicacion(new Coordenada(val)));
                    break;
                case "modo":
                    this.addFiltro(new FiltroPorModo(val));
                    break;
                default:
                    break;
            }
        });
    }

    public static Criterio nuevo() {
        return new Criterio();
    }

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