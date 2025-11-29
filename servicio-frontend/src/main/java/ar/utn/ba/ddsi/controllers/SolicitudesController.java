package ar.utn.ba.ddsi.controllers;

import ar.utn.ba.ddsi.models.dto.input.CategoriaDTO;
import ar.utn.ba.ddsi.models.dto.input.HechoDTO;
import ar.utn.ba.ddsi.models.dto.input.SolicitudDeEliminacionDTO;
import ar.utn.ba.ddsi.models.dto.output.HechoOutputDTO;
import ar.utn.ba.ddsi.models.dto.output.SolicitudDeEliminacionOutputDTO;
import ar.utn.ba.ddsi.services.IAgregadorService;
import ar.utn.ba.ddsi.services.IDinamicaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    log.info("Solicitud recibida: " + solicitud);
    try {
      agregadorService.solicitarEliminacion(solicitud);
      redirectAttributes.addFlashAttribute("exito", "La solicitud ha sido creada");
      return "redirect:/api/solicitudes/eliminacion/" + id;
    } catch (Exception ex) {
      redirectAttributes.addFlashAttribute("error", "Error en la solicitud");
      return "redirect:/solicitudes/eliminacion/" + id;
    }
  }

  @GetMapping("/modificacion/{id_hecho}")
  public String formularioSolicitarModificacion(@PathVariable("id_hecho") Long id_hecho, Model model, RedirectAttributes redirectAttributes){
    List<CategoriaDTO> categorias = agregadorService.pedirCategorias();
    List<HechoDTO> hechosDelContribuyente = agregadorService.pedirHechosDeContribuyente();   // Hay que probar esto

    try{
      HechoDTO hecho = hechosDelContribuyente.stream().filter(h -> h.getId().equals(id_hecho)).findFirst().get();
      HechoOutputDTO hechoOutputDTO = HechoOutputDTO.fromDTOtoOutput(hecho);

        model.addAttribute("hecho", hechoOutputDTO);
        model.addAttribute("id_hecho", id_hecho);
        model.addAttribute("categorias", categorias);

        return "main-page/crearSolicitudDeModificacion";
    }catch (Exception ex){
      return "error/403";
    }
  }

  @PostMapping("/solicitarModificacion")
  public String solicitarModificacion(@ModelAttribute("hecho") HechoOutputDTO hecho, @RequestParam(name = "id_hecho") Long id_hecho, RedirectAttributes redirectAttributes, @RequestParam(value = "fotos", required = false) List<MultipartFile> imagenes) {
      log.info("DTO recibido: {}", hecho + ", Id recibido: " + id_hecho);
     try{
        dinamicaService.modificarHecho(id_hecho, hecho);
        redirectAttributes.addFlashAttribute("exito", "La solicitud ha sido creada");
        return "redirect:/api/solicitudes/modificacion/" + id_hecho;
      }catch (Exception ex){
       redirectAttributes.addFlashAttribute("error", "Error al crear la solicitud");
       return "redirect:/solicitudes/modificacion/" + id_hecho;
      }

  }

    // ENDPOINTS: administrador

    @GetMapping("/tratarEliminaciones")
    public String solicitudesPendientes(Model model){
      List<Long> idsPendientes = agregadorService.pedirIDsPendientes();
      if (idsPendientes.isEmpty()) {
        return "error/sinSolicitudesDeEliminacionPendientes";
      }
      return "redirect:/api/solicitudes/tratarEliminacion/" + idsPendientes.get(0);

    }

    @GetMapping("/tratarEliminacion/{solicitudId}")
    public String formularioTratarSolicitudDeEliminacion(@PathVariable("solicitudId") Long solicitudId, Model model){
      SolicitudDeEliminacionDTO solicitud = agregadorService.pedirSolicitudDeEliminacion(solicitudId);
      List<Long> idsPendientes = agregadorService.pedirIDsPendientes();
      log.info("Solicitud a tratar: " + solicitud);

      int indiceActual = idsPendientes.indexOf(solicitudId);
      Long anteriorId = (indiceActual > 0) ? idsPendientes.get(indiceActual - 1) : null;
      Long siguienteId = (indiceActual < idsPendientes.size() - 1) ? idsPendientes.get(indiceActual + 1) : null;

      model.addAttribute("solicitud", solicitud);
      model.addAttribute("anteriorId", anteriorId);
      model.addAttribute("siguienteId", siguienteId);

      return "main-page/tratarSolicitudDeEliminacion";
    }

    /*
  @PostMapping("/solicitudesDeEliminacion/resolverEliminacion")
  public String procesarSolicitudDeEliminacion(@RequestParam Long solicitudId, @RequestParam String accion) {

    if ("aprobar".equals(accion)) {
      agregadorService.aceptarSolicitud(solicitudId);
    } else if ("rechazar".equals(accion)) {
      agregadorService.rechazarSolicitud(solicitudId);
    }


    Long proximaSolicitudId = agregadorService.obtenerPrimerIdSolicitudPendiente();

    // 3. Redirigir
    if (proximaSolicitudId != null) {
      // Redirigir a la vista de la próxima solicitud
      return "redirect:/solicitudesDeEliminacion/" + proximaSolicitudId;
    } else {
      // No quedan solicitudes
      return "redirect:/dashboard-admin?mensaje=solicitudes_completadas";
    }
  }
  */


}



