package com.tecsup.services;

import com.tecsup.dto.MedicoRequestDTO;
import com.tecsup.exception.DuplicateResourceException;
import com.tecsup.exception.ResourceNotFoundException;
import com.tecsup.model.Medico;
import com.tecsup.repository.EspecialidadRepository;
import com.tecsup.repository.MedicoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MedicoService {

    private final MedicoRepository repo;
    private final EspecialidadRepository especialidadRepo;

    public MedicoService(MedicoRepository repo, EspecialidadRepository especialidadRepo) {
        this.repo = repo;
        this.especialidadRepo = especialidadRepo;
    }

    @Transactional(readOnly = true)
    public List<Medico> listar() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public List<Medico> listarPorEspecialidad(Long idEspecialidad) {
        return repo.findByIdEspecialidad(idEspecialidad);
    }

    @Transactional(readOnly = true)
    public List<Medico> listarPorEstado(String estado) {
        return repo.findByEstado(estado);
    }

    @Transactional(readOnly = true)
    public Medico obtener(Long id) {
        return repo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Médico no encontrado con id: " + id));
    }

    @Transactional
    public Medico crear(MedicoRequestDTO dto) {
        String cmp = dto.getCmp().trim();
        if (repo.existsByCmp(cmp)) {
            throw new DuplicateResourceException("Ya existe un médico con el CMP: " + cmp);
        }
        validarEspecialidad(dto.getIdEspecialidad());

        Medico medico = new Medico();
        aplicarDatos(medico, dto, cmp);
        return repo.save(medico);
    }

    @Transactional
    public Medico actualizar(Long id, MedicoRequestDTO dto) {
        Medico medico = obtener(id);
        String cmp = dto.getCmp().trim();

        if (!cmp.equals(medico.getCmp()) && repo.existsByCmp(cmp)) {
            throw new DuplicateResourceException("Ya existe un médico con el CMP: " + cmp);
        }
        validarEspecialidad(dto.getIdEspecialidad());

        aplicarDatos(medico, dto, cmp);
        return repo.save(medico);
    }

    @Transactional
    public void eliminar(Long id) {
        Medico medico = obtener(id);
        repo.delete(medico);
    }

    private void validarEspecialidad(Long idEspecialidad) {
        if (!especialidadRepo.existsById(idEspecialidad)) {
            throw new ResourceNotFoundException(
                    "Especialidad no encontrada con id: " + idEspecialidad);
        }
    }

    private void aplicarDatos(Medico medico, MedicoRequestDTO dto, String cmp) {
        medico.setCmp(cmp);
        medico.setNombres(dto.getNombres().trim());
        medico.setApellidos(dto.getApellidos().trim());
        medico.setTelefono(dto.getTelefono());
        medico.setCorreo(dto.getCorreo());
        medico.setIdEspecialidad(dto.getIdEspecialidad());
        medico.setEstado(dto.getEstado() == null ? "A" : dto.getEstado());
    }
}
