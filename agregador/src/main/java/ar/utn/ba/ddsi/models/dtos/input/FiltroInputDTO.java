package ar.utn.ba.ddsi.models.dtos.input;

import ar.utn.ba.ddsi.commons.Coordenada;
import ar.utn.ba.ddsi.models.entities.Categoria;
import ar.utn.ba.ddsi.models.entities.filtros.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class FiltroInputDTO {
    private String tipoDeFiltro;
    private Map<String,Object> parametros;
    
    public Filtro toEntity(){
        if(this.getTipoDeFiltro().equals("titulo")){
            return new FiltroPorTitulo((String) this.getParametros().get("titulo"));
        }else if(this.getTipoDeFiltro().equals("descripcion")){
            return new FiltroPorDescripcion((String) this.getParametros().get("descripcion"));
        }else if(this.getTipoDeFiltro().equals("categoria")){
            return new FiltroPorCategoria(new Categoria((String) this.getParametros().get("nombre")));
        }else if(this.getTipoDeFiltro().equals("ubicacion")){
            String latStr = (String) parametros.get("latitud");
            String lonStr = (String) parametros.get("longitud");
            Double lat = Double.valueOf(latStr);
            Double lon = Double.valueOf(lonStr);
            return new FiltroPorUbicacion(new Coordenada(lat, lon));
        }else if(this.getTipoDeFiltro().equals("fechaHecho")){
            LocalDateTime desde = LocalDateTime.parse((String) this.getParametros().get("desde"));
            LocalDateTime hasta = LocalDateTime.parse((String) this.getParametros().get("hasta"));
            return new FiltroPorFechaHecho(desde,hasta);
        }else if(this.getTipoDeFiltro().equals("fechaDeCarga")){
            return new FiltroPorFechaDeCarga((String) this.getParametros().get("desde"),(String) this.getParametros().get("hasta"));
        }else{
            throw new RuntimeException("Tipo de filtro no encontrado");
        }
    }
}