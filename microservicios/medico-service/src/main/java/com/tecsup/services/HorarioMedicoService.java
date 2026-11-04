package com.tecsup.services;

import com.tecsup.dto.HorarioRequestDTO;
import com.tecsup.exception.ResourceNotFoundException;
import com.tecsup.model.HorarioMedico;
import com.tecsup.repository.HorarioMedicoRepository;
import com.tecsup.repository.MedicoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HorarioMedicoService {

    private final HorarioMedicoRepository repo;
    private final MedicoRepository medicoRepo;

    public HorarioMedicoService(HorarioMedicoRepository repo, MedicoRepository medicoRepo) {
        this.repo = repo;
        this.medicoRepo = medicoRepo;
    }

    @Transactional(readOnly = true)
    public List<HorarioMedico> listar() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public List<HorarioMedico> listarPorMedico(Long idMedico) {
        return repo.findByIdMedico(idMedico);
    }

    @Transactional(readOnly = true)
    public HorarioMedico obtener(Long id) {
        return repo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Horario no encontrado con id: " + id));
    }

    @Transactional
    public HorarioMedico crear(HorarioRequestDTO dto) {
        validarMedico(dto.getIdMedico());
        validarRangoHorario(dto);

        HorarioMedico horario = new HorarioMedico();
        aplicarDatos(horario, dto);
        return repo.save(horario);
    }

    @Transactional
    public HorarioMedico actualizar(Long id, HorarioRequestDTO dto) {
        HorarioMedico horario = obtener(id);
        validarMedico(dto.getIdMedico());
        validarRangoHorario(dto);

        aplicarDatos(horario, dto);
        return repo.save(horario);
    }

    @Transactional
    public void eliminar(Long id) {
        HorarioMedico horario = obtener(id);
        repo.delete(horario);
    }

    private void validarMedico(Long idMedico) {
        if (!medicoRepo.existsById(idMedico)) {
            throw new ResourceNotFoundException("Médico no encontrado con id: " + idMedico);
        }
    }

    private void validarRangoHorario(HorarioRequestDTO dto) {
        if (!dto.getHoraInicio().isBefore(dto.getHoraFin())) {
            throw new IllegalArgumentException(
                    "La hora de inicio debe ser anterior a la hora de fin");
        }
    }

    private void aplicarDatos(HorarioMedico horario, HorarioRequestDTO dto) {
        horario.setIdMedico(dto.getIdMedico());
        horario.setDiaSemana(dto.getDiaSemana());
        horario.setHoraInicio(dto.getHoraInicio());
        horario.setHoraFin(dto.getHoraFin());
    }
}
