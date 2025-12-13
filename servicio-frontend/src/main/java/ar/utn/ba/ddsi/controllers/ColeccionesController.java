package ar.utn.ba.ddsi.controllers;


import ar.utn.ba.ddsi.models.dto.input.*;
import ar.utn.ba.ddsi.models.dto.output.ColeccionOutputDTO;
import ar.utn.ba.ddsi.models.dto.output.CriterioOutputDTO;
import ar.utn.ba.ddsi.models.dto.output.HechoOutputDTO;
import ar.utn.ba.ddsi.services.IAgregadorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/colecciones")
public class ColeccionesController {

    @Autowired
    private IAgregadorService agregadorService;

    @GetMapping("/formulario-de-carga")
    public String formularioCrearColeccion(Model model) {
        try {
            List<FuenteDTO> fuentes = agregadorService.buscarFuentes();
            log.info("fuentes recibidas: " + fuentes);
            List<CategoriaDTO> categorias = agregadorService.pedirCategorias();
            model.addAttribute("coleccionOutputDTO", new ColeccionOutputDTO());
            model.addAttribute("fuentes", fuentes);
            model.addAttribute("categorias", categorias);
            return "main-page/crearColeccion";
        } catch (Exception e) {
            model.addAttribute("errorFuentes", "Error al intentar cargar la página");
            return "redirect:/404";
        }
    }

    @PostMapping("/cargar")
    public String cargarColeccion(@ModelAttribute("coleccionDTO") ColeccionOutputDTO coleccion, RedirectAttributes redirectAttributes) {
       try {
           log.info("DTO recibido: {}", coleccion);
           List<String> identificadores = agregadorService.pedirIdentificadoresDeColecciones();

           if (coleccion.getCriterioDePertenencia() == null) {
               coleccion.setCriterioDePertenencia(new CriterioOutputDTO());
           }

           String idRecibido = coleccion.getIdentificador().trim().toLowerCase();
           List<String> identificadoresEnMinusculas = identificadores.stream()
                   .map(String::toLowerCase)
                   .toList();

           // Esta línea debería funcionar.... no funciona
           if (identificadoresEnMinusculas.contains(idRecibido)) {
               redirectAttributes.addFlashAttribute("repetición", "El identificador ya existe");
               return "redirect:/colecciones/formulario-de-carga";
           }

           agregadorService.cargarColeccion(coleccion);
           redirectAttributes.addFlashAttribute("exito", "Colección creada con éxito");
           return "redirect:/colecciones/formulario-de-carga";
       } catch (Exception e) {
           redirectAttributes.addFlashAttribute("error", "Ocurrió un error inesperado");
           log.error(e.getMessage());
           return "redirect:/colecciones/formulario-de-carga";
       }
    }


    @GetMapping("/formulario-de-edicion/{id_coleccion}")
    public String formularioEditarColeccion(@PathVariable("id_coleccion") String id_coleccion, Model model, RedirectAttributes redirectAttributes) {

        try{
            ColeccionDTO coleccion = agregadorService.pedirColeccionPorId(id_coleccion);
            List<CategoriaDTO> categorias = agregadorService.pedirCategorias();
            List<FuenteDTO> fuentes = agregadorService.buscarFuentes();

            ColeccionOutputDTO coleccionOutputDTO = ColeccionOutputDTO.fromDTOtoOutput(coleccion);

            model.addAttribute("coleccionOutputDTO", coleccionOutputDTO);
            model.addAttribute("categorias", categorias);
            model.addAttribute("fuentes", fuentes);
            return "main-page/editarColeccion";
        } catch (Exception ex) {
            return "error/404";
        }
    }

    @PostMapping("/editar")
    public String editarColeccion(@ModelAttribute("coleccionOutputDTO") ColeccionOutputDTO coleccion, RedirectAttributes redirectAttributes) {
        try {
            log.info("DTO recibido: {}", coleccion);
            agregadorService.actualizarColeccion(coleccion);
            redirectAttributes.addFlashAttribute("actualizada", "Colección actualizada");
            return "redirect:/colecciones";

        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", "Ocurrió un error inesperado al actualizar la colección");
            return "redirect:/colecciones/formulario-de-edicion/" + coleccion.getIdentificador();
        }
    }


    @GetMapping
    public String mostrarColecciones(Model model){
        List<ColeccionDTO> colecciones = agregadorService.pedirColecciones();
        model.addAttribute("colecciones", colecciones);
        return "administrar-colecciones";
    }


    @PostMapping("/eliminar/{id_coleccion}")
    public String eliminarColeccion(@PathVariable("id_coleccion") String id_coleccion, RedirectAttributes redirectAttributes){
        try {
            agregadorService.eliminarColeccion(id_coleccion);
            redirectAttributes.addFlashAttribute("exito", "La colección ha sido eliminada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo eliminar la colección.");
        }
        return "redirect:/colecciones";
    }


    @GetMapping("/con-hechos")
    public String mostrarColeccionConHechos(Model model){
        List<ColeccionConHechosDTO> colecciones = agregadorService.pedirColeccionesConHechos();
        model.addAttribute("colecciones", colecciones);
        return "";
    }

    @GetMapping("/con-hechos-curados")
    public String mostrarColeccionesConHechosCurados(Model model){
        List<ColeccionConHechosDTO> colecciones = agregadorService.pedirColeccionesConHechosCurados();
        model.addAttribute("colecciones", colecciones);
        return "administrar-colecciones";
    }


    @GetMapping("/{id}/con-hechos")
    public String mostrarColeccionPorId(@PathVariable("id") String id, Model model, @RequestParam(name = "page", defaultValue = "0") int page,
                                        @RequestParam(name = "size", defaultValue = "10") int size,
                                        @RequestParam(name = "modo", defaultValue = "irrestricta") String modo){
        ColeccionConHechosDTO coleccion = agregadorService.pedirColeccionConHechos(id, page, size, modo);
        log.info("Colección traida: " + coleccion);
        model.addAttribute("coleccion", coleccion);
        model.addAttribute("currentMode", modo);
        model.addAttribute("isIrrestricta", modo.equals("irrestricta"));
        return "main-page/verColeccion";
    }


}