package ar.utn.ba.ddsi.controllers;

import ar.utn.ba.ddsi.services.IPathsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/datasets")
public class PathsController {
    @Autowired
    private IPathsService pathsService;

    @PostMapping
    public ResponseEntity<String> importarCSVs(@RequestParam("files") List<MultipartFile> files){
        if (files.isEmpty()) {
            return ResponseEntity.badRequest().body("No se enviaron archivos.");
        }
        try {
            this.pathsService.guardarCSVs(files);
            return ResponseEntity.ok("Se procesaron " + files.size() + " archivos con éxito.");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al procesar los archivos: " + e.getMessage());
        }
    }
}