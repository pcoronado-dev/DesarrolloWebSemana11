package com.tecsup.repository;

import com.tecsup.model.Cita;
import com.tecsup.model.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Long> {

    List<Cita> findByIdPaciente(Long idPaciente);

    List<Cita> findByIdMedico(Long idMedico);

    List<Cita> findByFechaOrderByHoraAsc(LocalDate fecha);

    List<Cita> findByEstado(EstadoCita estado);
}
