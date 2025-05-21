package ar.utn.ba.ddsi.controllers;

import ar.utn.ba.ddsi.models.dtos.outputs.HechoDTO;
import ar.utn.ba.ddsi.services.impl.fuenteProxyServices;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/hechos")
public class fuenteProxyController {
  private fuenteProxyServices fuenteProxyServices;

  public fuenteProxyController(fuenteProxyServices fuenteProxyServices) {
    this.fuenteProxyServices = fuenteProxyServices;
  }

  @GetMapping
  public Mono<List<HechoDTO>> getAllHechos(){
    return fuenteProxyServices.getAll();
  }

  @GetMapping("/sync")
  public List<HechoDTO> getProducts(){
    return this.fuenteProxyServices.getAll().block();
  }

  @GetMapping("/{id}")
  public Mono<HechoDTO> getById(@PathVariable Long id){
    return fuenteProxyServices.getById(id);
  }

}
