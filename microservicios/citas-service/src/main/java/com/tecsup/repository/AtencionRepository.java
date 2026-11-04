package com.tecsup.repository;

import com.tecsup.model.Atencion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AtencionRepository extends JpaRepository<Atencion, Long> {

    Optional<Atencion> findByIdCita(Long idCita);

    boolean existsByIdCita(Long idCita);
}
