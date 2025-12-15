package ar.utn.ba.ddsi.controllers;

import
        ar.utn.ba.ddsi.commons.CustomUserDetails;
import ar.utn.ba.ddsi.models.dtos.output.HechoOutputDTO;
import ar.utn.ba.ddsi.models.entities.Hecho;
import ar.utn.ba.ddsi.services.IHechosService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
@Slf4j
@RestController
@RequestMapping("/api/hechos")
@CrossOrigin(origins = "*")
public class HechosController {
    private IHechosService hechosService;

    @Autowired
    public HechosController(IHechosService hechosService) {
        this.hechosService = hechosService;
    }

    @GetMapping
    public Page<HechoOutputDTO> buscarTodos(@RequestParam Map<String, String> parametros, @PageableDefault(size = 10, page = 0) Pageable pageable) {
        return this.hechosService.buscarTodos(parametros, pageable);
    }

    @GetMapping("{id}")
    public HechoOutputDTO buscarHecho(@PathVariable Long id) {
        HechoOutputDTO hechoDTO = this.hechosService.buscarHecho(id);
        return hechoDTO;
    }

    @GetMapping("/disponible/{id}")
    public HechoOutputDTO buscarHechoNoEliminado(@PathVariable Long id) {
        return this.hechosService.buscarHechoNoEliminado(id);
    }

    @GetMapping("/disponible/cantidad")
    public Integer pedirCantidadDeHechosEnElSistema(){
        return this.hechosService.pedirCantidadDeHechosEnElSistema();
    }


    @GetMapping("/contribuyente")
    public Page<HechoOutputDTO> buscarHechoContribuyente(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam Map<String, String> parametros,
            @PageableDefault(size = 10, page = 0) Pageable pageable
    ) {
        Long contribuyenteId = userDetails.getId();
        return hechosService.obtenerPorContribuyente(contribuyenteId, parametros, pageable);
    }

    @GetMapping("/destacados/{cantidad_hechos_destacados}")
    public List<HechoOutputDTO> buscarHechosDestacados(@PathVariable Integer cantidad_hechos_destacados) {
        LocalDateTime fecha = LocalDateTime.now();
        List<HechoOutputDTO> hechosDTO = this.hechosService.buscarHechos(fecha,cantidad_hechos_destacados);
        return hechosDTO;
    }

    @GetMapping("/ultimo")
    public HechoOutputDTO ultimoHechoCargado(){
        return hechosService.buscarUltimoHechoCargado();
    }


    @GetMapping("/dinamica/idsExternos")
    public List<Long> buscarIdsExternosDinamica(){
        return hechosService.buscarIdsExternosDinamica();
    }

    @GetMapping("/dinamica/{id_externo}")
    public HechoOutputDTO buscarHechoDinamica(@PathVariable Long id_externo) {
        return this.hechosService.buscarHechoDinamica(id_externo);
    }

    @PostMapping("/actualizarTodos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> actualizarHechos() {
        try {
            SecurityContextHolder.clearContext();
            hechosService.actualizarHechos();
            log.info("El boton de actualizacion funciono");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
