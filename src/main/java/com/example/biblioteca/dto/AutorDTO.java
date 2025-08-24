package com.example.biblioteca.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutorDTO {

    private Long id;
    @NotNull
    @NotBlank(message = "El nombre no debe estar vacío")
    private String nombres;
    @NotNull @NotBlank(message = "El apellido no debe estar vacío")
    private String apellidos;
    @NotNull @NotBlank(message = "El pais no debe estar vacío")
    private String pais;
    @NotNull
    private char estado;
}
