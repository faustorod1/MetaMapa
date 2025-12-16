package ar.utn.ba.ddsi.controllers;

import ar.utn.ba.ddsi.commons.CustomUserDetails;
import ar.utn.ba.ddsi.models.dto.input.HechoInputDTO;
import ar.utn.ba.ddsi.models.dto.output.HechoOutputDTO;
import ar.utn.ba.ddsi.services.IHechosService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
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
  public Page<HechoOutputDTO> listarHechos(@PageableDefault(size = 100, page = 0) Pageable pageable) {
    return hechosService.getAll_DTO(pageable);
  }

  @GetMapping("/{id}")
  public HechoOutputDTO buscarHechoNoEliminado(@PathVariable Long id) {
    return hechosService.buscarHechoNoEliminado(id);
  }

  @GetMapping("/ids")
  public List<Long> buscarIdsHechos(){
    return hechosService.buscarIdsHechos();
  }

  @GetMapping(params = "desde") // localhost:8082/api/hechos?desde=algo
  public Page<HechoOutputDTO> buscarTodosCargadosDesde(
          @RequestParam("desde") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
          @PageableDefault(size = 100, page = 0) Pageable pageable
  ) {
    return this.hechosService.getAllDesde_DTO(desde, pageable);
  }

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public HechoOutputDTO crearHecho(@RequestPart("hecho") HechoInputDTO hecho, @RequestPart(value = "contenidosMultimedia", required = false)List<MultipartFile> archivos, @AuthenticationPrincipal CustomUserDetails userDetails){
    hecho.setContribuyenteId(userDetails.getId());
    return hechosService.crearHecho(hecho, archivos);
  }

  // Para que el agregador le avise cuando se elimina un hecho
  @DeleteMapping
  public void eliminarHecho(@RequestParam Long id){
    hechosService.marcarComoELiminado(id);
  }
}