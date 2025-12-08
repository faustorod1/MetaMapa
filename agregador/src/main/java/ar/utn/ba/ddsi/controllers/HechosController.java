package ar.utn.ba.ddsi.controllers;

import ar.utn.ba.ddsi.commons.CustomUserDetails;
import ar.utn.ba.ddsi.models.dtos.output.HechoOutputDTO;
import ar.utn.ba.ddsi.services.IHechosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hechos")
@CrossOrigin(origins = "*")
public class HechosController {
    private IHechosService hechosService;

    @Autowired
    public HechosController(IHechosService hechosService) {
        this.hechosService = hechosService;
    }

    @GetMapping
    public Page<HechoOutputDTO> buscarTodos(@RequestParam Map<String, String> parametros, @PageableDefault(size = 10, page = 0) Pageable pageable) {
        return this.hechosService.buscarTodos(parametros, pageable);
    }

    @GetMapping("{id}")
    public HechoOutputDTO buscarHecho(@PathVariable Long id) {
        HechoOutputDTO hechoDTO = this.hechosService.buscarHecho(id);
        return hechoDTO;
    }

    @GetMapping("/disponible/{id}")
    public HechoOutputDTO buscarHechoNoEliminado(@PathVariable Long id) {
        return this.hechosService.buscarHechoNoEliminado(id);
    }


    @GetMapping("/contribuyente")      // Le sacamos el {id} por ahora, ya que se uso queda reservado para validaciones contra el token
    public List<HechoOutputDTO> buscarHechoContribuyente(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long contribuyenteId = userDetails.getId();
        return hechosService.buscarHechoDe(contribuyenteId);
    }

    @GetMapping("/destacados/{cantidad_hechos_destacados}")
    public List<HechoOutputDTO> buscarHechosDestacados(@PathVariable Integer cantidad_hechos_destacados) {
        LocalDateTime fecha = LocalDateTime.now();
        List<HechoOutputDTO> hechosDTO = this.hechosService.buscarHechos(fecha,cantidad_hechos_destacados);
        return hechosDTO;
    }

    @GetMapping("/dinamica/idsExternos")
    public List<Long> buscarIdsExternosDinamica(){
        return hechosService.buscarIdsExternosDinamica();
    }

    @GetMapping("/dinamica/{id_externo}")
    public HechoOutputDTO buscarHechoDinamica(@PathVariable Long id_externo) {
        return this.hechosService.buscarHechoDinamica(id_externo);
    }


}
