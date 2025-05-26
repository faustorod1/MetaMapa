package ar.utn.ba.ddsi.controllers;

import ar.utn.ba.ddsi.models.dtos.externals.HechoDTO;
import ar.utn.ba.ddsi.models.dtos.outputs.HechoOutputDTO;
import ar.utn.ba.ddsi.services.impl.fuenteProxyServices;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/hechos")
public class fuenteProxyController {
  private fuenteProxyServices fuenteProxyServices;

  public fuenteProxyController(fuenteProxyServices fuenteProxyServices) {
    this.fuenteProxyServices = fuenteProxyServices;
  }

  @GetMapping
  public List<HechoOutputDTO> getAll(){
    return fuenteProxyServices.getAll();
  }

  @GetMapping(params = "desde")
  public List<HechoOutputDTO> getAllDesde(@RequestParam("desde") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde){
    return fuenteProxyServices.getAllDesde(desde);
  }

  @GetMapping("/{id}")
  public HechoOutputDTO getById(@PathVariable Long id){
    return fuenteProxyServices.getById(id);
  }

/*
  @GetMapping("/sync")
  public List<HechoDTO> getProducts(){
    return this.fuenteProxyServices.getAll().block();
  }
*/

  @GetMapping("/metamapaInstance/{baseUrl}")
  public List<HechoOutputDTO> getHechosMetamapaInstance(@PathVariable String baseUrl){ return fuenteProxyServices.consumirMetamapa(baseUrl); }

}
