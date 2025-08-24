package com.example.biblioteca.controller.general;

import com.example.biblioteca.controller.error.BusinessException;
import com.example.biblioteca.controller.error.ResourceNotFoundException;
import com.example.biblioteca.dto.CategoriaDTO;
import com.example.biblioteca.service.general.service.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.Objects.isNull;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("api/v1/categorias")    
public class CategoriaController {
    private final CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<CategoriaDTO> cats =  categoriaService.findAll();
            if (isNull(cats) || cats.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok().body(cats);
        } catch (Exception e) {
            log.info("Error: " + e);
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDTO> obtenerPorId(@PathVariable Long id) {
        CategoriaDTO categoria = categoriaService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría con id " + id + " no existe"));
        return ResponseEntity.ok(categoria);
    }

    @PostMapping
    public ResponseEntity<CategoriaDTO> crear(@Valid @RequestBody CategoriaDTO dto) {
        if (dto.getId() != null) {
            throw new BusinessException("No se permite crear con ID predefinido");
        }
        CategoriaDTO nueva = categoriaService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDTO> actualizar(@PathVariable Long id, @Valid @RequestBody CategoriaDTO dto) {
        CategoriaDTO actual = categoriaService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la categoría con id " + id));

        dto.setId(id);
        CategoriaDTO actualizado = categoriaService.update(dto);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        CategoriaDTO categoria = categoriaService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe categoría con id " + id));

        if ("I".equals(categoria.getEstado())) {
            throw new BusinessException("La categoría ya está inactiva");
        }

        categoriaService.deleteLogic(id);
        return ResponseEntity.ok("Categoría desactivada correctamente");
    }
}
