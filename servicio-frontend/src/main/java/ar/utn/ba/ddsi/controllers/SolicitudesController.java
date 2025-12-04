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
import jakarta.persistence.EntityNotFoundException;
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
    SolicitudDeEliminacionOutputDTO solicitud = new SolicitudDeEliminacionOutputDTO();
    solicitud.setHechoId(id);
    model.addAttribute("solicitud", solicitud);
    HechoDTO hecho = agregadorService.pedirHecho(id);
    model.addAttribute("hecho", hecho);
    return "crearSolicitudDeEliminacion";
  }

  @PostMapping("/solicitarEliminacion")
  public String solicitarEliminacion(@ModelAttribute("solicitud") SolicitudDeEliminacionOutputDTO solicitud, RedirectAttributes redirectAttributes) {
    Long id = solicitud.getHechoId();
    try {
      agregadorService.solicitarEliminacion(solicitud);
      redirectAttributes.addFlashAttribute("exito", "La solicitud ha sido creada");
      return "redirect:/api/solicitudes/eliminacion/" + id;
    } catch (Exception ex) {
      redirectAttributes.addFlashAttribute("error", "Error en la solicitud");
      return "redirect:/solicitudes/eliminacion/" + id;
    }
  }



  // TODO: el hecho no debe poder ser modificado si ya fue eliminado
  // TODO: desplegar correctamente las categorías

  @GetMapping("/modificacion/{id_hecho}")
  public String formularioSolicitarModificacion(@PathVariable("id_hecho") Long id_hecho, Model model, RedirectAttributes redirectAttributes) {
    List<CategoriaDTO> categorias = agregadorService.pedirCategorias();
    List<HechoDTO> hechosDelContribuyente = agregadorService.pedirHechosDeContribuyente();   // Hay que probar esto
    try {
      HechoDTO hecho = agregadorService.pedirHecho(id_hecho);
      boolean pertenece = hechosDelContribuyente.stream().anyMatch(h -> h.getId().equals(id_hecho));
      if (!pertenece) {
        return "error/403";
      }

      HechoOutputDTO hechoOutputDTO = HechoOutputDTO.fromDTOtoOutput(hecho);

      model.addAttribute("hecho", hechoOutputDTO);
      model.addAttribute("id_hecho", id_hecho);
      model.addAttribute("categorias", categorias);

      return "main-page/crearSolicitudDeModificacion";

    } catch (EntityNotFoundException ex) {
      return "error/404";
    }
  }

  @PostMapping("/solicitarModificacion")
  public String solicitarModificacion(@ModelAttribute("hecho") HechoOutputDTO hecho, @RequestParam(name = "id_hecho") Long id_hecho, RedirectAttributes redirectAttributes, @RequestParam(value = "fotos", required = false) List<MultipartFile> imagenes) {
    try {
      dinamicaService.modificarHecho(id_hecho, hecho);
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


// Esto está complicado: ya que la solicitud de modificacion tiene referenciando al id_hecho de la fuente dinámica.
// Trato de ver cuando pueda como solucionar todo. Tendría que usar ids externos, eso lo se.
/*
  @GetMapping("/tratarModificaciones")
  public String modificacionesPendientes() {
    List<Long> idsPendientes = dinamicaService.pedirIDsModificacionesPendientes();

    if (idsPendientes.isEmpty()) {
      return "error/sinSolicitudesDeModificacionPendientes";
    }
    List<Long> idsHechosValidos = agregadorService.pedirIDsExternosHechos();

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
   // try {
      SolicitudDeModificacionDTO solicitud = dinamicaService.pedirSolicitudDeModificacion(solicitudId);
      HechoDTO hecho = agregadorService.pedirHecho(solicitud.getIdHecho());

      List<Long> idsPendientes = dinamicaService.pedirIDsModificacionesPendientes();
      List<Long> idsHechosValidos = dinamicaService.pedirIDsHechos();
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
   // } catch (Exception e) {
     //   return "error/404";
   // }
  }


  @PostMapping("/solicitudesDeModificacion/resolverModificacion")
  public String procesarSolicitudDeModificacion(@RequestParam("hechoId") Long hechoId, @RequestParam("accion") String accion, @RequestParam("motivoDeEstado") String motivoDeEstado, RedirectAttributes redirectAttributes) {
    ResolucionSolicitudDeModificacionOutputDTO resolucion = new ResolucionSolicitudDeModificacionOutputDTO();
    resolucion.setMotivoDeEstado(motivoDeEstado);

    if(accion.equals("aceptada")) {
      resolucion.setEstadoNuevo(EstadoSolicitud.ACEPTADA);
    }else if(accion.equals("rechazada")) {
      resolucion.setEstadoNuevo(EstadoSolicitud.RECHAZADA);
    }else {
      resolucion.setEstadoNuevo(EstadoSolicitud.ACEPTADA_CON_SUGERENCIA);
    }

   dinamicaService.resolverModificacion(hechoId, resolucion);
   return "redirect:/api/solicitudes/tratarModificaciones";

  }
*/

}



