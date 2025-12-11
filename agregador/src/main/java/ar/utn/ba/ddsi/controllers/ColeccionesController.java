package ar.utn.ba.ddsi.controllers;

import ar.utn.ba.ddsi.commons.CustomUserDetails;
import ar.utn.ba.ddsi.models.dtos.input.CriterioInputDTO;
import ar.utn.ba.ddsi.models.dtos.input.FuenteDTO;
import ar.utn.ba.ddsi.models.dtos.output.ColeccionConHechosCuradosOutputDTO;
import ar.utn.ba.ddsi.models.dtos.output.ColeccionConHechosOutputDTO;
import ar.utn.ba.ddsi.models.dtos.output.ColeccionOutputDTO;
import ar.utn.ba.ddsi.models.dtos.output.HechoOutputDTO;
import ar.utn.ba.ddsi.models.dtos.input.ColeccionInputDTO;
import ar.utn.ba.ddsi.services.IColeccionesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
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
    public Page<HechoOutputDTO> buscarHechosPorColeccion(@PathVariable String identificador, @RequestParam Map<String, String> parametros, @PageableDefault(size = 10, page = 0) Pageable pageable) {
        return this.coleccionesService.buscarHechosPorColeccion(identificador, parametros, pageable);
    }

    @GetMapping("/{identificador}/con-hechos")
    public ColeccionConHechosOutputDTO buscarColeccionConHechos(@PathVariable String identificador, @RequestParam Map<String, String> parametros, @PageableDefault(size = 10, page = 0) Pageable pageable) {
        ColeccionConHechosOutputDTO coleccion = this.coleccionesService.buscarColeccionConHechos(identificador, parametros, pageable);
        log.info("Coleccion: " + coleccion);
        return coleccion;
    }


    @GetMapping("/destacadas/{cantidad_colecciones_destacadas}")
    public List<ColeccionOutputDTO> buscarColeccionesDestacadas(@PathVariable Integer cantidad_colecciones_destacadas) {
        LocalDateTime fecha = LocalDateTime.now();
        return this.coleccionesService.buscarUltimasColecciones(fecha,cantidad_colecciones_destacadas);
    }


    // API Administrativa ----------------------------------------------------------------------------------------------

    @GetMapping("/{identificador}")
    public ColeccionOutputDTO buscarPorId(@PathVariable String identificador) {
        return coleccionesService.buscarPorId(identificador);
    }

    @GetMapping("/con-hechos-curados")
    public List<ColeccionConHechosCuradosOutputDTO> buscarTodosConHechosCurados(){
        return this.coleccionesService.buscarTodosConHechosCurados();
    }

    @GetMapping("/identificadores")
    public List<String> buscarIdentificadores(){
        return coleccionesService.buscarIdentificadores();
    }

    @PostMapping("/cargar")
    public ColeccionOutputDTO crearColeccion(@RequestBody ColeccionInputDTO coleccionInputDTO){
        log.info("Entró en crearColeccion:" + coleccionInputDTO);
        return coleccionesService.crearColeccion(coleccionInputDTO);
    }

    @PutMapping
    public ColeccionOutputDTO actualizarColeccion(@RequestBody ColeccionInputDTO coleccionInputDTO){
        log.info("Entró en actualizarColeccion:" + coleccionInputDTO);
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

    @DeleteMapping("/{identificador}")
    public void eliminarColeccion(@PathVariable String identificador){
        log.info("Id recibido: " + identificador);
        coleccionesService.eliminarColeccion(identificador);
    }

    @GetMapping("/fuentes")
    public List<FuenteDTO> buscarFuentes(){
        return coleccionesService.buscarFuentes();
    }

}
