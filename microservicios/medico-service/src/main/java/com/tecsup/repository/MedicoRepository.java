package com.tecsup.repository;

import com.tecsup.model.Medico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MedicoRepository extends JpaRepository<Medico, Long> {

    Optional<Medico> findByCmp(String cmp);

    boolean existsByCmp(String cmp);

    List<Medico> findByIdEspecialidad(Long idEspecialidad);

    List<Medico> findByEstado(String estado);
}
