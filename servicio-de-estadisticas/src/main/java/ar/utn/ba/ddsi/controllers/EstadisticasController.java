package ar.utn.ba.ddsi.controllers;

import ar.utn.ba.ddsi.models.entities.Categoria;
import ar.utn.ba.ddsi.models.entities.SolicitudDeEliminacion;
import ar.utn.ba.ddsi.services.IEstadisticasService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;


@RestController
public class EstadisticasController {

    private IEstadisticasService estadisticasService;

    @GetMapping
    public ProvinciaDTO provinciaConMasHechos(Categoria categoria){ //@RequestParam????
        return estadisticasService.provinciaConMasHechos(Categoria categoria);
    }

    @GetMapping
    public String categoriaConMasHechos(){
        return estadisticasService.categoriaConMasHechos();
    }

    @GetMapping
    public ProvinciaDTO provinciaConMayorCantHechos(Categoria categoria){
        return
    }

    @GetMapping
    public LocalTime horarioConMasHechosPorCategoria(Categoria categoria){
        return estadisticasService.horarioConMasHechosDeCiertaCategoria(categoria);

    }

    @GetMapping
    public Integer cuantasSonSpam(SolicitudDeEliminacion solicitud){
        return
    }
    /*
De una colección, ¿en qué provincia se agrupan la mayor cantidad de hechos reportados?
¿Cuál es la categoría con mayor cantidad de hechos reportados?
¿En qué provincia se presenta la mayor cantidad de hechos de una cierta categoría?
¿A qué hora del día ocurren la mayor cantidad de hechos de una cierta categoría?
¿Cuántas solicitudes de eliminación son spam?

El Servicio de Estadísticas deberá soportar la exportación de los datos generados mediante formato CSV.
*/

}
