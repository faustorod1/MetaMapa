package ar.utn.ba.ddsi.controllers;

import ar.utn.ba.ddsi.models.dtos.input.CriterioInputDTO;
import ar.utn.ba.ddsi.models.dtos.input.FuenteDTO;
import ar.utn.ba.ddsi.models.dtos.output.ColeccionConHechosOutputDTO;
import ar.utn.ba.ddsi.models.dtos.output.ColeccionOutputDTO;
import ar.utn.ba.ddsi.models.dtos.output.HechoOutputDTO;
import ar.utn.ba.ddsi.models.dtos.input.ColeccionInputDTO;
import ar.utn.ba.ddsi.services.IColeccionesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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

    @GetMapping("/{identificador}/hechos")
    public List<HechoOutputDTO> buscarHechosPorColeccion(@PathVariable String identificador, @RequestParam Map<String, String> parametros) {
        return this.coleccionesService.buscarHechosPorColeccion(identificador, parametros);
    }

    @GetMapping("/destacadas/{cantidad_colecciones_destacadas}")
    public List<ColeccionOutputDTO> buscarColeccionesDestacadas(@PathVariable Integer cantidad_colecciones_destacadas) {
        LocalDateTime fecha = LocalDateTime.now();
        return this.coleccionesService.buscarUltimasColecciones(fecha,cantidad_colecciones_destacadas);
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
    public ColeccionOutputDTO actualizarFuentes(@PathVariable String identificador, @RequestBody List<Long> idsFuentes){
        return coleccionesService.updateFuentes(identificador, idsFuentes);
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

    @GetMapping("/fuentes")
    public List<FuenteDTO> buscarFuentes(){
        return coleccionesService.buscarFuentes();
    }

}
