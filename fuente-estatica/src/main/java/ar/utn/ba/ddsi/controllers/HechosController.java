package ar.utn.ba.ddsi.controllers;

import java.util.List;

import ar.utn.ba.ddsi.models.dtos.output.HechoOutputDTO;
import ar.utn.ba.ddsi.services.IHechosService;
import ar.utn.ba.ddsi.services.impl.HechosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/hechos")
public class HechosController {
    @Autowired
    private IHechosService hechosService;

    @GetMapping
    public List<HechoOutputDTO> buscarTodos() {
        return this.hechosService.buscarTodos();
    }

    /*
    @GetMapping("/{id}")
    public HechoOutputDTO buscarPorId(Long id) {
        return this.hechosService.buscarPorId(id);
    }
     */


}
