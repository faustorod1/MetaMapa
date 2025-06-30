package ar.utn.ba.ddsi.controllers;

import ar.utn.ba.ddsi.models.dtos.input.CriterioInputDTO;
import ar.utn.ba.ddsi.models.dtos.output.ColeccionOutputDTO;
import ar.utn.ba.ddsi.models.dtos.output.HechoOutputDTO;
import ar.utn.ba.ddsi.models.dtos.input.ColeccionInputDTO;
import ar.utn.ba.ddsi.models.entities.Criterio;
import ar.utn.ba.ddsi.services.IColeccionesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/colecciones")
public class ColeccionesController {

    @Autowired
    private IColeccionesService coleccionesService;

    // API Pública -----------------------------------------------------------------------------------------------------

    @GetMapping
    public List<ColeccionOutputDTO> buscarTodos() {
        return this.coleccionesService.buscarTodos();
    }

    @GetMapping("/{identificador}/{modo}/hechos")
    public List<HechoOutputDTO> buscarHechosPorColeccion(@PathVariable String identificador,@PathVariable String modo, @RequestParam Map<String, String> parametros) {
        return this.coleccionesService.buscarHechosPorColeccion(identificador,modo, parametros).block();
    }

    // API Administrativa ----------------------------------------------------------------------------------------------

    @PostMapping
    public ColeccionOutputDTO crearColeccion(@RequestBody ColeccionInputDTO coleccionInputDTO){
        return coleccionesService.crearColeccion(coleccionInputDTO);
    }

    @PutMapping
    public ColeccionOutputDTO actualizarColeccion(@RequestBody ColeccionInputDTO coleccionInputDTO){
        return coleccionesService.updateColeccion(coleccionInputDTO);
    }

    @PatchMapping("{identificador}/fuentes")
    public ColeccionOutputDTO actualizarFuentes(@PathVariable String identificador, @RequestBody List<String> fuentes){
        return coleccionesService.updateFuentes(identificador, fuentes);
    }


    @PatchMapping("/{identificador}/criterio")
    public ColeccionOutputDTO actualizarCriterio(@PathVariable String identificador, @RequestBody CriterioInputDTO criterioInputDTO){
        return coleccionesService.updateCriterio(identificador, criterioInputDTO);
    }

    @DeleteMapping
    public void EliminarColeccion(@RequestBody String identificador){
        coleccionesService.eliminarColeccion(identificador);
    }

}
