package ar.utn.ba.ddsi.controllers;

import ar.utn.ba.ddsi.models.dtos.outputs.HechoOutputDTO;
import ar.utn.ba.ddsi.services.impl.HechosService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/hechos")
public class HechosController {

  private HechosService hechosService;

  public HechosController(HechosService hechosService) {
    this.hechosService = hechosService;
  }

  @GetMapping
  public Page<HechoOutputDTO> getAll(@PageableDefault(size = 100, page = 0) Pageable pageable){
    return hechosService.getAll(pageable);
  }

  @GetMapping(params = "desde")
  public Page<HechoOutputDTO> getAllDesde(
          @RequestParam("desde") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
          @PageableDefault(size = 100, page = 0) Pageable pageable
  ) {
    return hechosService.getAllDesde(desde, pageable);
  }

  @GetMapping("/metamapaInstance")
  public List<HechoOutputDTO> getHechosMetamapaInstance() {
    return hechosService.getAllFromMetamapa();
  }

  @GetMapping("/metamapaInstanceAfter")
  public List<HechoOutputDTO> getHechosMetamapaInstanceDesde(@RequestParam("desde") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde) {
    return hechosService.getAllFromMetamapaDesde(desde);
  }

//  @PatchMapping
//  public void eliminarHecho(@RequestParam Long id, @RequestParam Long APIid){
//    hechosService.marcarComoEliminado(id, APIid);
//  }

}
