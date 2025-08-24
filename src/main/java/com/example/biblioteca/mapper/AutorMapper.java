package com.example.biblioteca.mapper;

import com.example.biblioteca.dto.AutorDTO;
import com.example.biblioteca.entity.Autor;
import org.mapstruct.Mapper;


import java.util.List;

@Mapper(componentModel = "spring")
public interface AutorMapper {
    AutorDTO toDto(Autor autor);
    Autor toEntity(AutorDTO dto);
    List<AutorDTO> toDtoList(List<Autor> autores);

}