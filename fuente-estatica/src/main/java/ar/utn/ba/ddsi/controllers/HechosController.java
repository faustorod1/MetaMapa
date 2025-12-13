package ar.utn.ba.ddsi.controllers;

import java.time.LocalDateTime;
import java.util.List;

import ar.utn.ba.ddsi.models.dtos.output.HechoOutputDTO;
import ar.utn.ba.ddsi.services.IHechosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/hechos")
public class HechosController {
    @Autowired
    private IHechosService hechosService;

    @GetMapping(params = "desde")
    public Page<HechoOutputDTO> buscarTodosCargadosDesde(
            @RequestParam("desde") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @PageableDefault(size = 100, page = 0) Pageable pageable
    ) {
        return this.hechosService.obtenerHechosCargadosDesde(desde, pageable);
    }



}
