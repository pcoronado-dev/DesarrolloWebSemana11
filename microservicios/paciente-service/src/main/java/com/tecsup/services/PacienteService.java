package com.tecsup.services;

import com.tecsup.dto.PacienteRequestDTO;
import com.tecsup.exception.DuplicateResourceException;
import com.tecsup.exception.ResourceNotFoundException;
import com.tecsup.model.Paciente;
import com.tecsup.repository.PacienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PacienteService {

    private final PacienteRepository repo;

    public PacienteService(PacienteRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<Paciente> listar() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public List<Paciente> listarPorEstado(String estado) {
        return repo.findByEstado(estado);
    }

    @Transactional(readOnly = true)
    public Paciente obtener(Long id) {
        return repo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Paciente no encontrado con id: " + id));
    }

    @Transactional
    public Paciente crear(PacienteRequestDTO dto) {
        String dni = dto.getDni().trim();
        if (repo.existsByDni(dni)) {
            throw new DuplicateResourceException("Ya existe un paciente con el DNI: " + dni);
        }

        Paciente paciente = new Paciente();
        aplicarDatos(paciente, dto, dni);
        return repo.save(paciente);
    }

    @Transactional
    public Paciente actualizar(Long id, PacienteRequestDTO dto) {
        Paciente paciente = obtener(id);
        String dni = dto.getDni().trim();

        if (!dni.equals(paciente.getDni()) && repo.existsByDni(dni)) {
            throw new DuplicateResourceException("Ya existe un paciente con el DNI: " + dni);
        }

        aplicarDatos(paciente, dto, dni);
        return repo.save(paciente);
    }

    @Transactional
    public void eliminar(Long id) {
        Paciente paciente = obtener(id);
        repo.delete(paciente);
    }

    private void aplicarDatos(Paciente paciente, PacienteRequestDTO dto, String dni) {
        paciente.setDni(dni);
        paciente.setNombres(dto.getNombres().trim());
        paciente.setApellidos(dto.getApellidos().trim());
        paciente.setFechaNacimiento(dto.getFechaNacimiento());
        paciente.setSexo(dto.getSexo());
        paciente.setTelefono(dto.getTelefono());
        paciente.setDireccion(dto.getDireccion());
        paciente.setCorreo(dto.getCorreo());
        paciente.setEstado(dto.getEstado() == null ? "A" : dto.getEstado());
    }
}
