package com.tecsup.controller;

import com.tecsup.dto.CitaRequestDTO;
import com.tecsup.dto.EstadoCitaRequestDTO;
import com.tecsup.model.Cita;
import com.tecsup.model.EstadoCita;
import com.tecsup.services.CitaService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/citas")
public class CitaController {

    private final CitaService service;

    public CitaController(CitaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Cita> listar(
            @RequestParam(required = false) Long idPaciente,
            @RequestParam(required = false) Long idMedico,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(required = false) EstadoCita estado) {

        if (idPaciente != null) {
            return service.listarPorPaciente(idPaciente);
        }
        if (idMedico != null) {
            return service.listarPorMedico(idMedico);
        }
        if (fecha != null) {
            return service.listarPorFecha(fecha);
        }
        if (estado != null) {
            return service.listarPorEstado(estado);
        }
        return service.listar();
    }

    @GetMapping("/{id}")
    public Cita obtener(@PathVariable Long id) {
        return service.obtener(id);
    }

    @PostMapping
    public ResponseEntity<Cita> crear(@Valid @RequestBody CitaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @PutMapping("/{id}")
    public Cita actualizar(@PathVariable Long id,
                           @Valid @RequestBody CitaRequestDTO dto) {
        return service.actualizar(id, dto);
    }

    @PatchMapping("/{id}/estado")
    public Cita cambiarEstado(@PathVariable Long id,
                              @Valid @RequestBody EstadoCitaRequestDTO dto) {
        return service.cambiarEstado(id, dto.getEstado());
    }

    /** Cancela la cita cambiando su estado a CANCELADA (no elimina el registro). */
    @DeleteMapping("/{id}")
    public Cita cancelar(@PathVariable Long id) {
        return service.cancelar(id);
    }
}
