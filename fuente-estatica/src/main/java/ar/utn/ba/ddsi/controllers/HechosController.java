package ar.utn.ba.ddsi.controllers;

import java.time.LocalDateTime;
import java.util.List;

import ar.utn.ba.ddsi.models.dtos.output.HechoOutputDTO;
import ar.utn.ba.ddsi.services.IHechosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/hechos")
public class HechosController {
    @Autowired
    private IHechosService hechosService;

    @GetMapping("/getAll")
    public List<HechoOutputDTO> buscarTodos() {
        return this.hechosService.buscarTodos();
    }

    @GetMapping
    public List<HechoOutputDTO> buscarTodosCargadosDesde(
            @RequestParam("desde") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde) {
        return this.hechosService.obtenerHechosCargadosDesde(desde);
    }


}
