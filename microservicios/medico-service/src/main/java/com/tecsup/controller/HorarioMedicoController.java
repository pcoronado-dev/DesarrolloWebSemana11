package com.tecsup.controller;

import com.tecsup.dto.HorarioRequestDTO;
import com.tecsup.model.HorarioMedico;
import com.tecsup.services.HorarioMedicoService;
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
@RequestMapping("/api/horarios")
public class HorarioMedicoController {

    private final HorarioMedicoService service;

    public HorarioMedicoController(HorarioMedicoService service) {
        this.service = service;
    }

    @GetMapping
    public List<HorarioMedico> listar(@RequestParam(required = false) Long idMedico) {
        if (idMedico != null) {
            return service.listarPorMedico(idMedico);
        }
        return service.listar();
    }

    @GetMapping("/{id}")
    public HorarioMedico obtener(@PathVariable Long id) {
        return service.obtener(id);
    }

    @PostMapping
    public ResponseEntity<HorarioMedico> crear(@Valid @RequestBody HorarioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @PutMapping("/{id}")
    public HorarioMedico actualizar(@PathVariable Long id,
                                    @Valid @RequestBody HorarioRequestDTO dto) {
        return service.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
