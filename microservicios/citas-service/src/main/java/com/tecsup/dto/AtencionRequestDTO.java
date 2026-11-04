package com.tecsup.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class AtencionRequestDTO {

    @NotNull(message = "La cita es obligatoria")
    @Positive(message = "El id de la cita debe ser mayor que cero")
    private Long idCita;

    @NotBlank(message = "El diagnóstico es obligatorio")
    @Size(max = 2000, message = "El diagnóstico no debe superar los 2000 caracteres")
    private String diagnostico;

    @Size(max = 2000, message = "El tratamiento no debe superar los 2000 caracteres")
    private String tratamiento;

    @Size(max = 2000, message = "Las observaciones no deben superar los 2000 caracteres")
    private String observaciones;

    public Long getIdCita() {
        return idCita;
    }

    public void setIdCita(Long idCita) {
        this.idCita = idCita;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
