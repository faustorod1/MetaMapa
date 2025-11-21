package ar.utn.ba.ddsi.controllers;
import ar.utn.ba.ddsi.models.dto.input.CategoriaDTO;
import ar.utn.ba.ddsi.services.IAgregadorService;
import lombok.extern.slf4j.Slf4j;

import ar.utn.ba.ddsi.exceptions.HechoMalCargadoException;
import ar.utn.ba.ddsi.models.dto.output.HechoOutputDTO;
import ar.utn.ba.ddsi.models.dto.input.HechoDTO;
import ar.utn.ba.ddsi.services.IDinamicaService;
import ar.utn.ba.ddsi.services.IEstaticaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/hechos")
public class HechosController {

    @Autowired
    private IDinamicaService dinamicaService;
    @Autowired
    private IEstaticaService estaticaService;
    @Autowired
    private IAgregadorService agregadorService;

    // ENDPOINTS: contribuyentes

    @GetMapping("/formulario-de-carga")
    public String formularioCargarHecho(Model model) {
        List<CategoriaDTO> categorias = agregadorService.pedirCategorias();
        model.addAttribute("hechoOutputDTO", new HechoOutputDTO());
        model.addAttribute("categorias", categorias);
        return "main-page/cargarHecho";      // Ahora ese endpoint tiene el HechoOutputDTO para cargarle los campos
    }

    @PostMapping("/cargar")
    public String cargarHecho(@ModelAttribute("hechoOutputDTO") HechoOutputDTO hechoOutputDTO, @RequestParam(value = "fotos", required = false) List<MultipartFile> imagenes, RedirectAttributes redirectAttributes) {
        try {
            log.info("DTO recibido: {}", hechoOutputDTO);
            dinamicaService.cargarHecho(hechoOutputDTO, imagenes);
            redirectAttributes.addFlashAttribute("mensaje", "Hecho creado con éxito");
            return "redirect:/hechos/formulario-de-carga";
       } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", "Ocurrió un error inesperado al intentar cargar el hecho");
            return "redirect:/hechos/formulario-de-carga";
        }

    }

    @GetMapping("detalle-hecho/{id_hecho}")
    public String detalleHecho(@PathVariable("id_hecho") Long id_hecho, Model model, RedirectAttributes redirectAttributes) {
        HechoDTO hecho = agregadorService.pedirHecho(id_hecho);
        model.addAttribute("hechoDTO", hecho);
        return "detalle-hecho";

    }



    // ENDPOINTS: administradores
    @GetMapping("importarCSV")
    public String formularioImportarCSV() {
        return "main-page/importarCSV";
    }

    @PostMapping("importar")
    public String importarCSVs(@RequestParam("files") List<MultipartFile> archivos, RedirectAttributes redirectAttributes) {

        try {
            estaticaService.importarCSVs(archivos);
            redirectAttributes.addFlashAttribute("exito", "Archivos correctamente importados");
            return "redirect:/hechos/importarCSV";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al importar los archivos");
            return "redirect:/hechos/importarCSV";
      }
    }

    }





