package ar.utn.ba.ddsi.controllers;

import ar.utn.ba.ddsi.models.dtos.output.HechoOutputDTO;
import ar.utn.ba.ddsi.services.IHechosService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
        return this.hechosService.buscarTodos(parametros).block();
    }

    // https://metamapa-avengers.com/api/hechos?categoria=caida%20aeronave&modo=curado&ubicacion=23,56
}
