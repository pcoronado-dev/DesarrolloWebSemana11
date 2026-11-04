package com.tecsup.services;

import com.tecsup.client.ReferenciaClient;
import com.tecsup.dto.CitaRequestDTO;
import com.tecsup.exception.ResourceNotFoundException;
import com.tecsup.model.Cita;
import com.tecsup.model.EstadoCita;
import com.tecsup.repository.CitaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class CitaService {

    private final CitaRepository repo;
    private final ReferenciaClient referenciaClient;

    public CitaService(CitaRepository repo, ReferenciaClient referenciaClient) {
        this.repo = repo;
        this.referenciaClient = referenciaClient;
    }

    @Transactional(readOnly = true)
    public List<Cita> listar() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public Cita obtener(Long id) {
        return repo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Cita no encontrada con id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Cita> listarPorPaciente(Long idPaciente) {
        return repo.findByIdPaciente(idPaciente);
    }

    @Transactional(readOnly = true)
    public List<Cita> listarPorMedico(Long idMedico) {
        return repo.findByIdMedico(idMedico);
    }

    @Transactional(readOnly = true)
    public List<Cita> listarPorFecha(LocalDate fecha) {
        return repo.findByFechaOrderByHoraAsc(fecha);
    }

    @Transactional(readOnly = true)
    public List<Cita> listarPorEstado(EstadoCita estado) {
        return repo.findByEstado(estado);
    }

    @Transactional
    public Cita crear(CitaRequestDTO dto) {
        referenciaClient.validarPaciente(dto.getIdPaciente());
        referenciaClient.validarMedico(dto.getIdMedico());

        Cita cita = new Cita();
        aplicarDatos(cita, dto);
        cita.setEstado(dto.getEstado() == null ? EstadoCita.PROGRAMADA : dto.getEstado());
        return repo.save(cita);
    }

    @Transactional
    public Cita actualizar(Long id, CitaRequestDTO dto) {
        Cita cita = obtener(id);
        referenciaClient.validarPaciente(dto.getIdPaciente());
        referenciaClient.validarMedico(dto.getIdMedico());

        aplicarDatos(cita, dto);
        return repo.save(cita);
    }

    @Transactional
    public Cita cambiarEstado(Long id, EstadoCita estado) {
        Cita cita = obtener(id);
        cita.setEstado(estado);
        return repo.save(cita);
    }

    @Transactional
    public Cita cancelar(Long id) {
        Cita cita = obtener(id);
        cita.setEstado(EstadoCita.CANCELADA);
        return repo.save(cita);
    }

    @Transactional
    public void eliminar(Long id) {
        Cita cita = obtener(id);
        repo.delete(cita);
    }

    private void aplicarDatos(Cita cita, CitaRequestDTO dto) {
        cita.setFecha(dto.getFecha());
        cita.setHora(dto.getHora());
        cita.setIdPaciente(dto.getIdPaciente());
        cita.setIdMedico(dto.getIdMedico());
        cita.setMotivo(dto.getMotivo());
    }
}
