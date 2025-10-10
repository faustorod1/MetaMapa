package ar.utn.ba.ddsi.controllers;
import lombok.extern.slf4j.Slf4j;

import ar.utn.ba.ddsi.exceptions.HechoMalCargadoException;
import ar.utn.ba.ddsi.models.dto.output.HechoOutputDTO;
import ar.utn.ba.ddsi.services.IDinamicaService;
import ar.utn.ba.ddsi.services.IEstaticaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

    // TODO: falta diseñar los banners ante un error o redirección

    @GetMapping("/formulario-de-carga")
    public String formularioCargarHecho(Model model) {
        model.addAttribute("hechoOutputDTO", new HechoOutputDTO());
        return "main-page/cargarHecho";      // Ahora ese endpoint tiene el HechoOutputDTO para cargarle los campos
    }

    @PostMapping("/cargar")
    public String cargarHecho(@ModelAttribute("hechoOutputDTO") HechoOutputDTO hechoOutputDTO, Model model, RedirectAttributes redirectAttributes) {
        try {
            log.info("DTO recibido: {}", hechoOutputDTO);
            dinamicaService.cargarHecho(hechoOutputDTO);
            redirectAttributes.addFlashAttribute("mensaje", "Hecho creado con éxito");
            return "redirect:/main";
        } catch (Exception ex) {
            model.addAttribute("error", "Ocurrió un error inesperado al intentar cargar el hecho");
            return "main-page/cargarHecho";
        }

    }

    @GetMapping("importarCSV")
    public String formularioImportarCSV(Model model) {
        return "main-page/importarCSV";
    }

    @PostMapping("importar")
    public String importarCSV(List<MultipartFile> archivos, Model model) {
        try {
            estaticaService.importarCSVs(archivos);
            model.addAttribute("exito", "Archivos correctamente importados");
            return "main-page/importarCSV";
        } catch (Exception e) {
            model.addAttribute("error", "Error al importar");
            return "main-page/importarCSV";
        }
    }
}



