package com.example.biblioteca.service.general.impl;

import com.example.biblioteca.dto.AutorDTO;
import com.example.biblioteca.entity.Autor;
import com.example.biblioteca.mapper.AutorMapper;
import com.example.biblioteca.repository.AutorRepository;
import com.example.biblioteca.service.general.service.AutorService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AutorServiceImpl implements AutorService {
    private final AutorRepository autorRepository;
    private final AutorMapper autorMapper;

    @Override
    public List<AutorDTO> findAll() throws ServiceException {
        List<Autor> list = autorRepository.findAll();
        return list.stream().map(l->autorMapper.toDto(l)).collect(Collectors.toList());
    }

    @Override
    public Optional<AutorDTO> findById(long id) throws ServiceException {
        return autorRepository.findById(id).map(l->autorMapper.toDto(l));
    }

    @Override
    public List<AutorDTO> findByObject(AutorDTO autorDTO) throws ServiceException {
        return List.of();
    }

    @Override
    public AutorDTO save(AutorDTO autorDTO) throws ServiceException {
        return autorMapper.toDto(autorRepository.save(autorMapper.toEntity(autorDTO)));
    }

    @Override
    public AutorDTO update(AutorDTO autorDTO) throws ServiceException {
        Optional<Autor> autor = autorRepository.findById(autorDTO.getId());
        if (autor.isPresent()) {
            Autor cat = autor.get();
            cat.setNombres(autorDTO.getNombres());
            cat.setApellidos(autorDTO.getApellidos());
            cat.setPais(autorDTO.getPais());
            cat.setEstado(autorDTO.getEstado());
            cat.setId(autorDTO.getId());
            return autorMapper.toDto(autorRepository.save(cat));
        }
        return null;
    }
    @Override
    @Transactional
    public void deleteLogic(Long id) throws ServiceException {
        try {
            Autor autor = autorRepository.findById(id)
                    .orElseThrow(() -> new ServiceException("El autor con id " + id + " no existe"));
            autor.setEstado('I');
            autorRepository.save(autor);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error al intentar eliminar el autor con id " + id, e);
        }
    }
}
