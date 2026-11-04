package com.tecsup.controller;

import com.tecsup.dto.MedicoRequestDTO;
import com.tecsup.model.Medico;
import com.tecsup.services.MedicoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/medicos")
public class MedicoController {

    private final MedicoService service;

    public MedicoController(MedicoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Medico> listar(@RequestParam(required = false) Long idEspecialidad,
                               @RequestParam(required = false) String estado) {
        if (idEspecialidad != null) {
            return service.listarPorEspecialidad(idEspecialidad);
        }
        if (estado != null && !estado.isBlank()) {
            return service.listarPorEstado(estado);
        }
        return service.listar();
    }

    @GetMapping("/{id}")
    public Medico obtener(@PathVariable Long id) {
        return service.obtener(id);
    }

    @PostMapping
    public ResponseEntity<Medico> crear(@Valid @RequestBody MedicoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @PutMapping("/{id}")
    public Medico actualizar(@PathVariable Long id,
                             @Valid @RequestBody MedicoRequestDTO dto) {
        return service.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
