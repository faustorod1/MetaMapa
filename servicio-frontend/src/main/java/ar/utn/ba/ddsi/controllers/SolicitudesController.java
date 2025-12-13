package ar.utn.ba.ddsi.controllers;

import ar.utn.ba.ddsi.models.dto.input.CategoriaDTO;
import ar.utn.ba.ddsi.models.dto.input.HechoDTO;
import ar.utn.ba.ddsi.models.dto.input.SolicitudDeEliminacionDTO;
import ar.utn.ba.ddsi.models.dto.input.SolicitudDeModificacionDTO;
import ar.utn.ba.ddsi.models.dto.output.HechoOutputDTO;
import ar.utn.ba.ddsi.models.dto.output.ResolucionSolicitudDeEliminacionOutputDTO;
import ar.utn.ba.ddsi.models.dto.output.ResolucionSolicitudDeModificacionOutputDTO;
import ar.utn.ba.ddsi.models.dto.output.SolicitudDeEliminacionOutputDTO;
import ar.utn.ba.ddsi.models.entities.EstadoSolicitud;
import ar.utn.ba.ddsi.services.IAgregadorService;
import ar.utn.ba.ddsi.services.IDinamicaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/api/solicitudes")
public class SolicitudesController {

  @Autowired
  private IAgregadorService agregadorService;
  @Autowired
  private IDinamicaService dinamicaService;


  // ENDPOINTS: Contribuyente
  @GetMapping("/eliminacion/{id}")
  public String formularioSolicitudEliminacion(@PathVariable("id") Long id, Model model) {
    try {
      SolicitudDeEliminacionOutputDTO solicitud = new SolicitudDeEliminacionOutputDTO();
      solicitud.setHechoId(id);
      model.addAttribute("solicitud", solicitud);
      HechoDTO hecho = agregadorService.pedirHecho(id);
      model.addAttribute("hecho", hecho);
      return "crearSolicitudDeEliminacion";
    } catch (Exception ex) {
      return "error/404";
    }
  }

@PostMapping("/solicitarEliminacion")
public String solicitarEliminacion(@ModelAttribute("solicitud") SolicitudDeEliminacionOutputDTO solicitud, RedirectAttributes redirectAttributes) {
    log.info("DTO recibido: {}", solicitud);
    Long id = solicitud.getHechoId();
   // try {
       SolicitudDeEliminacionDTO solicitudRecibida = agregadorService.solicitarEliminacion(solicitud);
       if(solicitudRecibida.getEstado() == (EstadoSolicitud.PENDIENTE)){
            redirectAttributes.addFlashAttribute("exito","La solicitud ha sido creada");
            return "redirect:/api/solicitudes/eliminacion/" + id;
       }else if (solicitudRecibida.getEstado().equals(EstadoSolicitud.RECHAZADA_POR_SPAM)){
            redirectAttributes.addFlashAttribute("spam","La solicitud fue rechazada por SPAM");
            return "redirect:/api/solicitudes/eliminacion/" + id;
       }else {
            redirectAttributes.addFlashAttribute("falta_caracteres","La solicitud fue rechazada por falta de carácteres");
            return "redirect:/api/solicitudes/eliminacion/" + id;
       }
   // }catch (Exception ex) {
    // redirectAttributes.addFlashAttribute("error","Ocurrió un error inesperado");
    // return "redirect:/api/solicitudes/eliminacion/" + id;
   // }
}



  @GetMapping("/modificacion/{id_hecho}")
  public String formularioSolicitarModificacion(@PathVariable("id_hecho") Long id_hecho, Model model, RedirectAttributes redirectAttributes) {
   try{
    List<CategoriaDTO> categorias = agregadorService.pedirCategorias();
        // List<HechoDTO> hechosDelContribuyente = agregadorService.pedirHechosDeContribuyente();   // Hay que probar esto

      HechoDTO hecho = agregadorService.pedirHecho(id_hecho);
      // log.info("hechos recibido: " + hechosDelContribuyente);
      Long id_externo_hecho = hecho.getIdExterno().getIdExterno();
      // boolean pertenece = hechosDelContribuyente.stream().anyMatch(h -> h.getId().equals(id_hecho));

      HechoOutputDTO hechoOutputDTO = HechoOutputDTO.fromDTOtoOutput(hecho);

      model.addAttribute("hecho", hechoOutputDTO);
      model.addAttribute("id_hecho", id_hecho);
      model.addAttribute("id_externo_hecho", id_externo_hecho);
      model.addAttribute("categorias", categorias);

      return "main-page/crearSolicitudDeModificacion";

   } catch (Exception ex) {
    return "error/404";
    }
  }

  @PostMapping("/solicitarModificacion")
  public String solicitarModificacion(@ModelAttribute("hecho") HechoOutputDTO hecho, @RequestParam(name = "id_hecho") Long id_hecho, @RequestParam(name = "id_externo_hecho") Long id_externo_hecho, RedirectAttributes redirectAttributes, @RequestParam(value = "fotos", required = false) List<MultipartFile> imagenes) {
    try {
      dinamicaService.modificarHecho(id_externo_hecho, hecho);
      redirectAttributes.addFlashAttribute("exito", "La solicitud ha sido creada");
      return "redirect:/api/solicitudes/modificacion/" + id_hecho;
    } catch (Exception ex) {
      redirectAttributes.addFlashAttribute("error", "Error al crear la solicitud");
      return "redirect:/solicitudes/modificacion/" + id_hecho;
    }

  }


  // ENDPOINTS: administrador

  @GetMapping("/tratarEliminaciones")
  public String eliminacionesPendientes() {
    List<Long> idsPendientes = agregadorService.pedirIDsEliminacionesPendientes();
    if (idsPendientes.isEmpty()) {
      return "error/sinSolicitudesDeEliminacionPendientes";
    }
    return "redirect:/api/solicitudes/tratarEliminacion/" + idsPendientes.get(0);

  }

  @GetMapping("/tratarEliminacion/{solicitudId}")
  public String formularioTratarSolicitudDeEliminacion(@PathVariable("solicitudId") Long solicitudId, Model model) {
    try {
      SolicitudDeEliminacionDTO solicitud = agregadorService.pedirSolicitudDeEliminacion(solicitudId);
      List<Long> idsPendientes = agregadorService.pedirIDsEliminacionesPendientes();
      HechoDTO hecho = agregadorService.pedirHecho(solicitud.getHechoId());
      LocalDate fechaSolicitud = solicitud.getFechaDeCarga().toLocalDate();

      int indiceActual = idsPendientes.indexOf(solicitudId);
      Long anteriorId = (indiceActual > 0) ? idsPendientes.get(indiceActual - 1) : null;
      Long siguienteId = (indiceActual < idsPendientes.size() - 1) ? idsPendientes.get(indiceActual + 1) : null;

      model.addAttribute("solicitud", solicitud);
      model.addAttribute("hecho", hecho);
      model.addAttribute("fechaSolicitud", fechaSolicitud);
      model.addAttribute("anteriorId", anteriorId);
      model.addAttribute("siguienteId", siguienteId);

      return "main-page/tratarSolicitudDeEliminacion";
    } catch (Exception e) {
      return "error/404";
    }
  }

  @PostMapping("/solicitudesDeEliminacion/resolverEliminacion")
  public String procesarSolicitudDeEliminacion(@RequestParam("solicitudId") Long solicitudId, @RequestParam("accion") String accion, RedirectAttributes redirectAttributes) {
    ResolucionSolicitudDeEliminacionOutputDTO resolucion = new ResolucionSolicitudDeEliminacionOutputDTO();

    try {
      if ("aprobar".equals(accion)) {
        resolucion.setEstadoSolicitud(EstadoSolicitud.ACEPTADA);
      } else if ("rechazar".equals(accion)) {
        resolucion.setEstadoSolicitud(EstadoSolicitud.RECHAZADA);
      }
      agregadorService.resolverEliminacion(solicitudId, resolucion);
    } catch (Exception ex) {
      redirectAttributes.addFlashAttribute("error", "La solicitud no pudo ser tratada");
      return "redirect:/api/solicitudes/tratarEliminacion/" + solicitudId;
    }

    return "redirect:/api/solicitudes/tratarEliminaciones";

}

  @GetMapping("/tratarModificaciones")
  public String modificacionesPendientes() {
    List<Long> idsPendientes = dinamicaService.pedirIDsModificacionesPendientes();

    if (idsPendientes.isEmpty()) {
      return "error/sinSolicitudesDeModificacionPendientes";
    }
    List<Long> idsHechosValidos = agregadorService.pedirIDsExternosDinamica();

    List<Long> idsPendientesValidos = idsPendientes.stream()
            .filter(idSolicitud -> {Long idHecho = dinamicaService
                      .pedirSolicitudDeModificacion(idSolicitud)
                      .getIdHecho();
              return idsHechosValidos.contains(idHecho);
            }).toList();

    if (idsPendientesValidos.isEmpty()) {
      return "error/sinSolicitudesDeModificacionPendientes";
    }
    Long idSolicitud = idsPendientesValidos.get(0);
    return "redirect:/api/solicitudes/tratarModificacion/" + idSolicitud;
  }

  @GetMapping("/tratarModificacion/{solicitudId}")
  public String formularioTratarSolicitudDeModificacion(@PathVariable("solicitudId") Long solicitudId, Model model) {
   try {
      SolicitudDeModificacionDTO solicitud = dinamicaService.pedirSolicitudDeModificacion(solicitudId);
      HechoDTO hecho = agregadorService.pedirHechoDinamica(solicitud.getIdHecho());

      List<Long> idsPendientes = dinamicaService.pedirIDsModificacionesPendientes();
      List<Long> idsHechosValidos = agregadorService.pedirIDsExternosDinamica();
      List<Long> idsPendientesValidos = idsPendientes.stream()
              .filter(idSolicitud -> {
                try {
                  Long idHecho = dinamicaService.pedirSolicitudDeModificacion(idSolicitud).getIdHecho();
                  return idsHechosValidos.contains(idHecho);
                } catch (Exception e) {
                  return false;
                }}).toList();

      if (!idsPendientesValidos.contains(solicitudId)) {
        return "error/404";
      }

      LocalDate fechaSolicitud = solicitud.getFechaDeCarga().toLocalDate();
      int indiceActual = idsPendientesValidos.indexOf(solicitudId);

      Long anteriorId = (indiceActual > 0) ? idsPendientesValidos.get(indiceActual - 1) : null;
      Long siguienteId = (indiceActual < idsPendientesValidos.size() - 1) ? idsPendientesValidos.get(indiceActual + 1) : null;

      model.addAttribute("hecho", hecho);
      model.addAttribute("solicitud", solicitud);
      model.addAttribute("fechaSolicitud", fechaSolicitud);
      model.addAttribute("anteriorId", anteriorId);
      model.addAttribute("siguienteId", siguienteId);

      return "main-page/tratarSolicitudDeModificacion";
    } catch (Exception e) {
        return "error/404";
    }
  }


  @PostMapping("/solicitudesDeModificacion/resolverModificacion")
  public String procesarSolicitudDeModificacion(@RequestParam("id_hecho_externo") Long id_hecho_externo, @RequestParam("accion") String accion, @RequestParam("motivoDeEstado") String motivoDeEstado, RedirectAttributes redirectAttributes) {
    ResolucionSolicitudDeModificacionOutputDTO resolucion = new ResolucionSolicitudDeModificacionOutputDTO();
    resolucion.setMotivoDeEstado(motivoDeEstado);

      switch (accion) {
          case "aceptada" -> resolucion.setEstadoNuevo(EstadoSolicitud.ACEPTADA);
          case "rechazada" -> resolucion.setEstadoNuevo(EstadoSolicitud.RECHAZADA);
          case "aceptada_con_sugerencia" -> resolucion.setEstadoNuevo(EstadoSolicitud.ACEPTADA_CON_SUGERENCIA);
          default -> {
              return "error/404";
          }
      }

    log.info("Resolucion a enviarse:" + resolucion);

   dinamicaService.resolverModificacion(id_hecho_externo, resolucion);
   return "redirect:/api/solicitudes/tratarModificaciones";

  }


}



