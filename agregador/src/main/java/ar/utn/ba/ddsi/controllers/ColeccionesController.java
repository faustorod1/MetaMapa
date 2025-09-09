package ar.utn.ba.ddsi.controllers;

import ar.utn.ba.ddsi.models.dtos.input.CriterioInputDTO;
import ar.utn.ba.ddsi.models.dtos.output.ColeccionConHechosOutputDTO;
import ar.utn.ba.ddsi.models.dtos.output.ColeccionOutputDTO;
import ar.utn.ba.ddsi.models.dtos.output.HechoOutputDTO;
import ar.utn.ba.ddsi.models.dtos.input.ColeccionInputDTO;
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

    // 1  https://metamapa-avengers.com/api/colecciones/1/hechos?categoria=caida%20aeronave&modo=curado&ubicacion=23,56
    // 2  https://metamapa-avengers.com/api/colecciones/1/curado/hechos?categoria=caida%20aeronave&ubicacion=23,56
    @GetMapping("/{identificador}/hechos")
    public List<HechoOutputDTO> buscarHechosPorColeccion(@PathVariable String identificador, @RequestParam Map<String, String> parametros) {
        return this.coleccionesService.buscarHechosPorColeccion(identificador, parametros).block();
    }

    // API Administrativa ----------------------------------------------------------------------------------------------

    @GetMapping("/con-hechos")
    public List<ColeccionConHechosOutputDTO> buscarTodosConHechos(){
        return this.coleccionesService.buscarTodosConHechos();
    }


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

    @PatchMapping("/{identificador}/tipoDeConsenso")
    public ColeccionOutputDTO actualizarConsenso(@PathVariable String identificador, @RequestBody String tipoDeConsenso){
        return coleccionesService.updateConsenso(identificador, tipoDeConsenso);
    }

    @DeleteMapping
    public void EliminarColeccion(@RequestBody String identificador){
        coleccionesService.eliminarColeccion(identificador);
    }

}
