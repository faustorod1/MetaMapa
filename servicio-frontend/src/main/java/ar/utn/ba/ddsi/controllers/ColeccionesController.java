package ar.utn.ba.ddsi.controllers;


import ar.utn.ba.ddsi.models.dto.input.ColeccionConHechosDTO;
import ar.utn.ba.ddsi.models.dto.input.ColeccionDTO;
import ar.utn.ba.ddsi.models.dto.input.FuenteDTO;
import ar.utn.ba.ddsi.models.dto.output.ColeccionOutputDTO;
import ar.utn.ba.ddsi.services.IAgregadorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
            model.addAttribute("coleccionOutputDTO", new ColeccionOutputDTO());
            model.addAttribute("fuentes", fuentes);
            return "main-page/crearColeccion";
        } catch (Exception e) {
            model.addAttribute("errorFuentes", "Error al intentar cargar la página");
            return "redirect:/404";
        }
    }

    @PostMapping("/cargar")
    public String cargarColeccion(@ModelAttribute("coleccionDTO") ColeccionOutputDTO coleccion, RedirectAttributes redirectAttributes) {
       log.info("DTO recibido: {}", coleccion);
       List<String> identificadores = agregadorService.pedirIdentificadoresDeColecciones();

       if(identificadores.contains(coleccion.getIdentificador())){
           redirectAttributes.addFlashAttribute("repetición", "El identificador ya existe");
           return "redirect:/colecciones/formulario-de-carga";
       }

       agregadorService.cargarColeccion(coleccion);
       redirectAttributes.addFlashAttribute("exito", "Colección creado con éxito");
       return "redirect:/colecciones/formulario-de-carga";

    }

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


}