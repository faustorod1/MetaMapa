package ar.utn.ba.ddsi.controllers;

import ar.utn.ba.ddsi.models.dto.input.CategoriaDTO;
import ar.utn.ba.ddsi.models.dto.input.HechoDTO;
import ar.utn.ba.ddsi.models.dto.input.SolicitudDeEliminacionDTO;
import ar.utn.ba.ddsi.models.dto.input.SolicitudDeModificacionDTO;
import ar.utn.ba.ddsi.models.dto.output.HechoOutputDTO;
import ar.utn.ba.ddsi.models.dto.output.ResolucionSolicitudDeEliminacionOutputDTO;
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
      HechoDTO hecho = hechosDelContribuyente.stream().filter(h -> h.getId().equals(id_hecho)).findFirst().get();
      HechoOutputDTO hechoOutputDTO = HechoOutputDTO.fromDTOtoOutput(hecho);

      model.addAttribute("hecho", hechoOutputDTO);
      model.addAttribute("id_hecho", id_hecho);
      model.addAttribute("categorias", categorias);

      return "main-page/crearSolicitudDeModificacion";
    } catch (Exception ex) {
      return "error/403";
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
    return "redirect:/api/solicitudes/tratarModificacion/" + idsPendientes.get(0);

  }


  @GetMapping("/tratarModificacion/{solicitudId}")
  public String formularioTratarSolicitudDeModificacion(@PathVariable("solicitudId") Long solicitudId, Model model) {
    SolicitudDeModificacionDTO solicitud = dinamicaService.pedirSolicitudDeModificacion(solicitudId);
    List<Long> idsPendientes = dinamicaService.pedirIDsModificacionesPendientes();
    HechoDTO hecho = agregadorService.pedirHecho(solicitud.getIdHecho());
    LocalDate fechaSolicitud = solicitud.getFechaDeCarga().toLocalDate();


    int indiceActual = idsPendientes.indexOf(solicitudId);
    Long anteriorId = (indiceActual > 0) ? idsPendientes.get(indiceActual - 1) : null;
    Long siguienteId = (indiceActual < idsPendientes.size() - 1) ? idsPendientes.get(indiceActual + 1) : null;

    model.addAttribute("hecho", hecho);
    model.addAttribute("solicitud", solicitud);
    model.addAttribute("fechaSolicitud", fechaSolicitud);
    model.addAttribute("anteriorId", anteriorId);
    model.addAttribute("siguienteId", siguienteId);

    return "main-page/tratarSolicitudDeModificacion";
  }





}



