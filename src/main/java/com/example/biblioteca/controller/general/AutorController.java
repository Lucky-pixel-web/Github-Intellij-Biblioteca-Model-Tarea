package com.example.biblioteca.controller.general;

import com.example.biblioteca.controller.error.BusinessException;
import com.example.biblioteca.controller.error.ResourceNotFoundException;
import com.example.biblioteca.dto.AutorDTO;
import com.example.biblioteca.service.general.service.AutorService;
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
@RequestMapping("api/v1/autores")
public class AutorController {
    private final AutorService autorService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<AutorDTO> cats = autorService.findAll();
            if (isNull(cats) || cats.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok().body(cats);
        } catch (Exception e) {
            log.info("Error: " + e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<AutorDTO> obtenerPorId(@PathVariable Long id) {
        AutorDTO autor = autorService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("autor con id " + id + " no existe"));
        return ResponseEntity.ok(autor);
    }

    @PostMapping
    public ResponseEntity<AutorDTO> crear(@Valid @RequestBody AutorDTO dto) {
        if (dto.getId() != null) {
            throw new BusinessException("No se permite crear con ID predefinido");
        }
        AutorDTO nueva = autorService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AutorDTO> actualizar(@PathVariable Long id, @Valid @RequestBody AutorDTO dto) {
        AutorDTO actual = autorService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el autor con id " + id));

        dto.setId(id);
        AutorDTO actualizado = autorService.update(dto);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        AutorDTO autor = autorService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe autor con id " + id));
        if ("I".equals(autor.getEstado())) {
            throw new BusinessException("El autor ya está inactivo");
        }

        autorService.deleteLogic(id);
        return ResponseEntity.ok("autor desactivado correctamente");
    }
}