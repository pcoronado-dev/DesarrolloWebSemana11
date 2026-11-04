package com.tecsup.services;

import com.tecsup.dto.AtencionRequestDTO;
import com.tecsup.exception.DuplicateResourceException;
import com.tecsup.exception.ResourceNotFoundException;
import com.tecsup.model.Atencion;
import com.tecsup.model.Cita;
import com.tecsup.model.EstadoCita;
import com.tecsup.repository.AtencionRepository;
import com.tecsup.repository.CitaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AtencionService {

    private final AtencionRepository repo;
    private final CitaRepository citaRepo;

    public AtencionService(AtencionRepository repo, CitaRepository citaRepo) {
        this.repo = repo;
        this.citaRepo = citaRepo;
    }

    @Transactional(readOnly = true)
    public List<Atencion> listar() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public Atencion obtener(Long id) {
        return repo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Atención no encontrada con id: " + id));
    }

    @Transactional(readOnly = true)
    public Atencion obtenerPorCita(Long idCita) {
        return repo.findByIdCita(idCita).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Atención no encontrada para la cita con id: " + idCita));
    }

    @Transactional
    public Atencion crear(AtencionRequestDTO dto) {
        Cita cita = citaRepo.findById(dto.getIdCita()).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Cita no encontrada con id: " + dto.getIdCita()));

        if (repo.existsByIdCita(dto.getIdCita())) {
            throw new DuplicateResourceException(
                    "La cita con id " + dto.getIdCita() + " ya tiene una atención registrada");
        }

        Atencion atencion = new Atencion();
        aplicarDatos(atencion, dto);
        Atencion guardada = repo.save(atencion);

        cita.setEstado(EstadoCita.ATENDIDA);
        citaRepo.save(cita);

        return guardada;
    }

    @Transactional
    public Atencion actualizar(Long id, AtencionRequestDTO dto) {
        Atencion atencion = obtener(id);

        if (!dto.getIdCita().equals(atencion.getIdCita())) {
            if (!citaRepo.existsById(dto.getIdCita())) {
                throw new ResourceNotFoundException(
                        "Cita no encontrada con id: " + dto.getIdCita());
            }
            if (repo.existsByIdCita(dto.getIdCita())) {
                throw new DuplicateResourceException(
                        "La cita con id " + dto.getIdCita() + " ya tiene una atención registrada");
            }
        }

        aplicarDatos(atencion, dto);
        return repo.save(atencion);
    }

    @Transactional
    public void eliminar(Long id) {
        Atencion atencion = obtener(id);
        repo.delete(atencion);
    }

    private void aplicarDatos(Atencion atencion, AtencionRequestDTO dto) {
        atencion.setIdCita(dto.getIdCita());
        atencion.setDiagnostico(dto.getDiagnostico());
        atencion.setTratamiento(dto.getTratamiento());
        atencion.setObservaciones(dto.getObservaciones());
    }
}
