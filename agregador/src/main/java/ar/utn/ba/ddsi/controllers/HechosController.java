package ar.utn.ba.ddsi.controllers;

import ar.utn.ba.ddsi.commons.CustomUserDetails;
import ar.utn.ba.ddsi.models.dtos.output.HechoOutputDTO;
import ar.utn.ba.ddsi.services.IHechosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hechos")
public class HechosController {
    private IHechosService hechosService;

    @Autowired
    public HechosController(IHechosService hechosService) {
        this.hechosService = hechosService;
    }

    @GetMapping
    public List<HechoOutputDTO> buscarTodos(@RequestParam Map<String, String> parametros) {
        return this.hechosService.buscarTodos(parametros);
    }

    @GetMapping("{id}")
    public HechoOutputDTO buscarHecho(@PathVariable Long id) {
        HechoOutputDTO hechoDTO = this.hechosService.buscarHecho(id);
        return hechoDTO;
    }

  @GetMapping("/contribuyente")      // Le sacamos el {id} por ahora, ya que se uso queda reservado para validaciones contra el token
  public List<HechoOutputDTO> buscarHechoContribuyente(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long contribuyenteId = userDetails.getId();
        return hechosService.buscarHechoDe(contribuyenteId);
    }
}
