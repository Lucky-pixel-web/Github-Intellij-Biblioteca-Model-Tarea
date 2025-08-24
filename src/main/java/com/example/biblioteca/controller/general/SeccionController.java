package com.example.biblioteca.controller.general;

import com.example.biblioteca.controller.error.BusinessException;
import com.example.biblioteca.controller.error.ResourceNotFoundException;
import com.example.biblioteca.dto.SeccionDTO;
import com.example.biblioteca.service.general.service.SeccionService;
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
@RequestMapping("api/v1/secciones")
public class SeccionController {
    private final SeccionService seccionService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<SeccionDTO> cats =  seccionService.findAll();
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
    public ResponseEntity<SeccionDTO> obtenerPorId(@PathVariable Long id) {
        SeccionDTO categoria = seccionService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría con id " + id + " no existe"));
        return ResponseEntity.ok(categoria);
    }

    @PostMapping
    public ResponseEntity<SeccionDTO> crear(@Valid @RequestBody SeccionDTO dto) {
        if (dto.getId() != null) {
            throw new BusinessException("No se permite crear con ID predefinido");
        }
        SeccionDTO nueva = seccionService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SeccionDTO> actualizar(@PathVariable Long id, @Valid @RequestBody SeccionDTO dto) {
        SeccionDTO actual = seccionService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la categoría con id " + id));

        dto.setId(id);
        SeccionDTO actualizado = seccionService.update(dto);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        SeccionDTO seccion = seccionService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe sección con id " + id));

        if ("I".equals(seccion.getEstado())) {
            throw new BusinessException("La sección ya está inactiva");
        }

        seccionService.deleteLogic(id);
        return ResponseEntity.ok("Sección desactivada correctamente");
    }
}
