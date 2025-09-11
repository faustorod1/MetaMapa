package ar.utn.ba.ddsi.models.dtos.output;

import ar.utn.ba.ddsi.commons.Coordenada;
import ar.utn.ba.ddsi.models.dtos.input.FiltroInputDTO;
import ar.utn.ba.ddsi.models.entities.Categoria;
import ar.utn.ba.ddsi.models.entities.filtros.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
public class FiltroOutputDTO {
    private String tipoDeFiltro;
    private Map<String,Object> parametros;



    public static FiltroOutputDTO fromEntity (Filtro filtro) {
        FiltroOutputDTO dto = new FiltroOutputDTO();
        if (filtro instanceof FiltroPorTitulo f) {
            dto.setTipoDeFiltro("titulo");
            dto.setParametros(new HashMap<>());
            dto.getParametros().put("titulo", f.getTitulo());
        } else if (filtro instanceof FiltroPorDescripcion f) {
            dto.setTipoDeFiltro("descripcion");
            dto.setParametros(new HashMap<>());
            dto.getParametros().put("descripcion", f.getDescripcion());
        } else if (filtro instanceof FiltroPorCategoria f) {
            dto.setTipoDeFiltro("categoria");
            dto.setParametros(new HashMap<>());
            dto.getParametros().put("categoria", f.getCategoria());
        } else if (filtro instanceof FiltroPorUbicacion f) {
            dto.setTipoDeFiltro("ubicacion");
            dto.setParametros(new HashMap<>());
            dto.getParametros().put("lugar", f.getLugar().comoArray());
        } else if (filtro instanceof FiltroPorFechaHecho f) {
            dto.setTipoDeFiltro("fechaHecho");
            dto.setParametros(new HashMap<>());
            dto.getParametros().put("desde", f.getDesde());
            dto.getParametros().put("hasta", f.getHasta());
        } else if (filtro instanceof FiltroPorFechaDeCarga f) {
            dto.setTipoDeFiltro("fechaDeCarga");
            dto.setParametros(new HashMap<>());
            dto.getParametros().put("desde", f.getDesde());
            dto.getParametros().put("hasta", f.getHasta());
        } else if (filtro instanceof FiltroPorEliminados f) {
            dto.setTipoDeFiltro("eliminados");
            dto.setParametros(new HashMap<>());
        } else {
            throw new RuntimeException("Tipo de filtro no encontrado (╯°□°)╯︵ ┻━┻");
        }

        return dto;
    }
}
