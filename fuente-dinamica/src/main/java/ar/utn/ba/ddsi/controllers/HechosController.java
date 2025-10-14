package ar.utn.ba.ddsi.controllers;

import ar.utn.ba.ddsi.commons.CustomUserDetails;
import ar.utn.ba.ddsi.models.dto.input.HechoInputDTO;
import ar.utn.ba.ddsi.models.dto.output.HechoOutputDTO;
import ar.utn.ba.ddsi.services.IHechosService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/hechos")
public class  HechosController {
  private IHechosService hechosService;

  public HechosController(IHechosService hechosService) {
    this.hechosService = hechosService;
  }

  @GetMapping
  public List<HechoOutputDTO> listarHechos(){
    return hechosService.getAll_DTO();
  }

  @GetMapping(params = "desde") // localhost:8082/api/hechos?desde=algo
  public List<HechoOutputDTO> buscarTodosCargadosDesde(@RequestParam("desde") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde){
    return this.hechosService.getAllDesde_DTO(desde);
  }

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public HechoOutputDTO crearHecho(@RequestPart("hecho") HechoInputDTO hecho, @RequestPart(value = "contenidosMultimedia")List<MultipartFile> archivos, @AuthenticationPrincipal CustomUserDetails userDetails){
    hecho.setContribuyenteId(userDetails.getId());
    return hechosService.crearHecho(hecho, archivos);
  }

  // Para que el agregador le avise cuando se elimina un hecho
  @DeleteMapping
  public void eliminarHecho(@RequestParam Long id){
    hechosService.marcarComoELiminado(id);
  }
}