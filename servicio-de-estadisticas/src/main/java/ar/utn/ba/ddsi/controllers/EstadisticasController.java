package ar.utn.ba.ddsi.controllers;

import ar.utn.ba.ddsi.models.entities.Categoria;
import ar.utn.ba.ddsi.models.entities.SolicitudDeEliminacion;
import ar.utn.ba.ddsi.services.IEstadisticasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;



@RestController
@RequestMapping("/api")
public class EstadisticasController {
    @Autowired
    private IEstadisticasService estadisticasService;

    @GetMapping("/colecciones/{id}/provincias/top")
    public ResponseEntity<String> provinciaConMasHechosDeColeccion(@PathVariable String id) {
        String provincia = estadisticasService.provinciaConMasHechosDeColeccion(id);
        return ResponseEntity.ok(provincia);
    }

    @GetMapping("/categorias/top")
    public ResponseEntity<String> categoriaConMasHechos(){
        String categoria = estadisticasService.categoriaConMasHechos();
        return ResponseEntity.ok(categoria);
    }

    @GetMapping("/categorias/{categoria}/provincias/top")
    public ResponseEntity<String> provinciaConMasHechosDeCategoria(@PathVariable String categoria) {
        String provincia = estadisticasService.provinciaConMasHechosDeCategoria(categoria);
        return ResponseEntity.ok(provincia);
    }

    @GetMapping("/categorias/{categoria}/horarios/top")
    public ResponseEntity<LocalTime> horarioConMasHechosPorCategoria(@PathVariable Categoria categoria){
        LocalTime horario = estadisticasService.horarioConMasHechosDeCiertaCategoria(categoria);
        return ResponseEntity.ok(horario);

    }

    @GetMapping("/solicitudes/cantidad-spam")
    public ResponseEntity<Long> cuantasSonSpam (){
        Long cantidad = estadisticasService.solicitudesSpam();
        return ResponseEntity.ok(cantidad);
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
