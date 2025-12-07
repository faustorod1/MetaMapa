package ar.utn.ba.ddsi.controllers;


import ar.utn.ba.ddsi.models.dto.input.*;
import ar.utn.ba.ddsi.models.dto.output.ColeccionOutputDTO;
import ar.utn.ba.ddsi.models.dto.output.CriterioOutputDTO;
import ar.utn.ba.ddsi.models.dto.output.HechoOutputDTO;
import ar.utn.ba.ddsi.services.IAgregadorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
           return "redirect:/colecciones/formulario-de-carga";
       }
    }

 /*
    @GetMapping("/editar/{id_coleccion}")
    public String formularioEditarColeccion(@PathVariable("id_coleccion") String identificador, Model model, RedirectAttributes redirectAttributes) {


        try{
            List<CategoriaDTO> categorias = agregadorService.pedirCategorias();
            List<HechoDTO> hechosDelContribuyente = agregadorService.pedirHechosDeContribuyente();   // Hay que probar esto

            HechoDTO hecho = agregadorService.pedirHecho(id_hecho);
            Long id_externo_hecho = hecho.getIdExterno().getIdExterno();
            boolean pertenece = hechosDelContribuyente.stream().anyMatch(h -> h.getId().equals(id_hecho));
            if (!pertenece) {
                return "error/403";
            }

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
            // HAY QUE COPIAR EL ESQUEMA DE LA EDICIÓN DEL HECHO
    */


/*
    @GetMapping()
    public String mostrarColeccionesConHechos(Model model){
        List<ColeccionConHechosDTO> colecciones = agregadorService.pedirColeccionesConHechos();
        model.addAttribute("colecciones", colecciones);
        return "";
    }

    @GetMapping()
    public String mostrarColeccionesConHechosCurados(Model model){
        List<ColeccionConHechosDTO> colecciones = agregadorService.pedirColeccionesConHechosCurados();
        model.addAttribute("colecciones", colecciones);
        return "";
    }

    */



}