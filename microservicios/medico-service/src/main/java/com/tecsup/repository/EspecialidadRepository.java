package com.tecsup.repository;

import com.tecsup.model.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EspecialidadRepository extends JpaRepository<Especialidad, Long> {

    Optional<Especialidad> findByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCase(String nombre);
}
