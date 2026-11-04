package com.tecsup.services;

import com.tecsup.dto.EspecialidadRequestDTO;
import com.tecsup.exception.DuplicateResourceException;
import com.tecsup.exception.ResourceNotFoundException;
import com.tecsup.model.Especialidad;
import com.tecsup.repository.EspecialidadRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EspecialidadService {

    private final EspecialidadRepository repo;

    public EspecialidadService(EspecialidadRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<Especialidad> listar() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public Especialidad obtener(Long id) {
        return repo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Especialidad no encontrada con id: " + id));
    }

    @Transactional
    public Especialidad crear(EspecialidadRequestDTO dto) {
        String nombre = dto.getNombre().trim();
        if (repo.existsByNombreIgnoreCase(nombre)) {
            throw new DuplicateResourceException("Ya existe la especialidad: " + nombre);
        }

        Especialidad especialidad = new Especialidad();
        especialidad.setNombre(nombre);
        especialidad.setDescripcion(dto.getDescripcion());
        especialidad.setEstado(dto.getEstado() == null ? "A" : dto.getEstado());
        return repo.save(especialidad);
    }

    @Transactional
    public Especialidad actualizar(Long id, EspecialidadRequestDTO dto) {
        Especialidad especialidad = obtener(id);
        String nombre = dto.getNombre().trim();

        if (!nombre.equalsIgnoreCase(especialidad.getNombre())
                && repo.existsByNombreIgnoreCase(nombre)) {
            throw new DuplicateResourceException("Ya existe la especialidad: " + nombre);
        }

        especialidad.setNombre(nombre);
        especialidad.setDescripcion(dto.getDescripcion());
        especialidad.setEstado(dto.getEstado() == null ? "A" : dto.getEstado());
        return repo.save(especialidad);
    }

    @Transactional
    public void eliminar(Long id) {
        Especialidad especialidad = obtener(id);
        repo.delete(especialidad);
    }
}
