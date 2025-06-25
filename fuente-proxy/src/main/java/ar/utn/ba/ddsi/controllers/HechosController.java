package ar.utn.ba.ddsi.controllers;

import ar.utn.ba.ddsi.models.dtos.outputs.HechoOutputDTO;
import ar.utn.ba.ddsi.services.impl.HechosService;
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
  public List<HechoOutputDTO> getAll(){
    return hechosService.getAll();
  }

  @GetMapping(params = "desde")
  public List<HechoOutputDTO> getAllDesde(@RequestParam("desde") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde){
    return hechosService.getAllDesde(desde);
  }

/*
  @GetMapping("/sync")
  public List<HechoDTO> getProducts(){
    return this.fuenteProxyServices.getAll().block();
  }
*/

  @GetMapping("/metamapaInstance")
  public List<HechoOutputDTO> getHechosMetamapaInstance() {
    return hechosService.getAllFromMetamapa();
  }

//  @GetMapping("/metamapaInstance/{baseUrl}")
//  public List<HechoOutputDTO> getHechosMetamapaInstance(@PathVariable String baseUrl){
//    return hechosService.consumirMetamapa(baseUrl);
//  }

  public void eliminarHecho(@RequestParam Long id, @RequestParam Long APIid){
    hechosService.marcarComoEliminado(id, APIid);
  }

}
