package ar.utn.ba.ddsi.controllers;

import ar.utn.ba.ddsi.services.IEstadisticasService;
import com.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;


@RestController
@RequestMapping("/api")

public class EstadisticasController {
    @Autowired
    private IEstadisticasService estadisticasService;

    @GetMapping("/colecciones/provincias/top/csv")
    public ResponseEntity<String> provinciaConMasHechosDeColeccionCSV() {
        String path = estadisticasService.getCSVPath("provincia-con-mas-hechos-de-coleccion");
        Path p = Paths.get(path);
        if (Files.notExists(p)) {
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.ok(path);
    }

    @GetMapping("/colecciones/top/csv")
    public ResponseEntity<String> categoriaConMasHechosCSV(){
        String path = estadisticasService.getCSVPath("categoria-con-mas-hechos");
        Path p = Paths.get(path);
        if (Files.notExists(p)) {
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.ok(path);
    }

    @GetMapping("/categorias/provincias/top/csv")
    public ResponseEntity<String> provinciaConMasHechosDeCategoriaCSV(){
        String path = estadisticasService.getCSVPath("provincia-con-mas-hechos-de-categoria");
        Path p = Paths.get(path);
        if (Files.notExists(p)) {
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.ok(path);
    }

    @GetMapping("categorias/horarios/top/csv")
    public ResponseEntity<String> horarioConMasHechosPorCategoriaCSV(){
        String path = estadisticasService.getCSVPath("horario-con-mas-hechos-por-categoria");
        Path p = Paths.get(path);
        if (Files.notExists(p)) {
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.ok(path);
    }

    @GetMapping("/solicitudes/cantidad-spam/csv")
    public ResponseEntity<String> cuantasSonSpamCSV(){
        String path = estadisticasService.getCSVPath("solicitudes-que-son-spam");
        Path p = Paths.get(path);
        if (Files.notExists(p)) {
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.ok(path);
    }
}
