package ar.utn.ba.ddsi.controllers;

import ar.utn.ba.ddsi.models.dtos.output.CategoriaDTO;
import ar.utn.ba.ddsi.models.entities.Categoria;
import ar.utn.ba.ddsi.models.repositories.ICategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriasController {
    @Autowired
    private ICategoriaRepository categoriaRepository;

    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> buscarTodos() {
        List<Categoria> categorias = categoriaRepository.findAll();
        List<CategoriaDTO> dtos = categorias.stream().map(CategoriaDTO::fromEntity).toList();
        return ResponseEntity.ok(dtos);
    }
    
    @PostMapping
    public ResponseEntity<CategoriaDTO> insertar(@RequestBody CategoriaDTO dto) {
        if (categoriaRepository.findByNombre(dto.getNombre()) != null) {
            return ResponseEntity.status(409).body(null);
        }
        Categoria categoria = dto.toEntity();
        categoriaRepository.save(categoria);
        return ResponseEntity.status(201).body(CategoriaDTO.fromEntity(categoria));
    }

    @PostMapping("/batch")
    public ResponseEntity<List<Long>> batchInsertar(@RequestBody List<CategoriaDTO> dtos) {
        List<String> nombres = dtos.stream().map(CategoriaDTO::getNombre).toList();
        List<String> categoriasRepetidas = categoriaRepository.findByNombreIn(nombres).stream().map(Categoria::getNombre).toList();

        if (!categoriasRepetidas.isEmpty()) {
            return ResponseEntity.status(409).body(null);
        }

        List<Categoria> categoriasPosta = dtos.stream().map(dto -> new Categoria(dto.getNombre())).toList();
        List<Categoria> creadas = categoriaRepository.saveAll(categoriasPosta);
        return ResponseEntity.status(201).body(creadas.stream().map(Categoria::getId).toList());
    }
}