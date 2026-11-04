package com.tecsup.controller;

import com.tecsup.dto.AtencionRequestDTO;
import com.tecsup.model.Atencion;
import com.tecsup.services.AtencionService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/atenciones")
public class AtencionController {

    private final AtencionService service;

    public AtencionController(AtencionService service) {
        this.service = service;
    }

    @GetMapping
    public List<Atencion> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Atencion obtener(@PathVariable Long id) {
        return service.obtener(id);
    }

    @GetMapping("/cita/{idCita}")
    public Atencion obtenerPorCita(@PathVariable Long idCita) {
        return service.obtenerPorCita(idCita);
    }

    @PostMapping
    public ResponseEntity<Atencion> crear(@Valid @RequestBody AtencionRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @PutMapping("/{id}")
    public Atencion actualizar(@PathVariable Long id,
                               @Valid @RequestBody AtencionRequestDTO dto) {
        return service.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
