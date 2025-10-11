package ar.utn.ba.ddsi.controllers;

import ar.utn.ba.ddsi.services.IPathsService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void importarCSVs(@RequestParam("files") List<MultipartFile> archivos){
        System.out.println("LLEGUÉ");
        this.pathsService.guardarCSVs(archivos);
    }

}
