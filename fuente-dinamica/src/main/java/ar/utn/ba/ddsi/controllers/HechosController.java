package ar.utn.ba.ddsi.controllers;

import ar.utn.ba.ddsi.models.dto.input.HechoInputDTO;
import ar.utn.ba.ddsi.models.dto.input.ResolucionDTO;
import ar.utn.ba.ddsi.models.dto.output.HechoOutputDTO;
import ar.utn.ba.ddsi.models.entities.Contribuyente;
import ar.utn.ba.ddsi.models.entities.Hecho;
import ar.utn.ba.ddsi.services.IHechosService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/hechos")
public class HechosController {
  private IHechosService hechosService;

  public HechosController(IHechosService hechosService) {
    this.hechosService = hechosService;
  }

  @GetMapping
  public List<HechoOutputDTO> listarHechos (){
    return hechosService.getAll_DTO();
  }

  @GetMapping(params = "desde") // localhost:8082/api/hechos?desde=algo
  public List<HechoOutputDTO> buscarTodosCargadosDesde(
          @RequestParam("desde") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde){
    return this.hechosService.getAllDesde_DTO(desde);
  }

  @PostMapping
  public HechoOutputDTO crearHecho(@RequestBody HechoInputDTO hecho){
    return hechosService.crearHecho(hecho);
  }


  // Para que el agregador le avise cuando se elimina un hecho
  @DeleteMapping
  public void eliminarHecho(@RequestParam Long id){
    hechosService.marcarComoELiminado(id);
  }
}