package com.tecsup.dto;

import com.tecsup.model.EstadoCita;
import jakarta.validation.constraints.NotNull;

public class EstadoCitaRequestDTO {

    @NotNull(message = "El estado es obligatorio")
    private EstadoCita estado;

    public EstadoCita getEstado() {
        return estado;
    }

    public void setEstado(EstadoCita estado) {
        this.estado = estado;
    }
}
