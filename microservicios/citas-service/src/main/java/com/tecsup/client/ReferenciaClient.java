package com.tecsup.client;

import com.tecsup.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

/**
 * Valida que los identificadores de paciente y médico existan en sus
 * respectivos microservicios. Si un servicio no está disponible, la
 * validación se omite para no acoplar la disponibilidad del sistema.
 */
@Component
public class ReferenciaClient {

    private static final Logger log = LoggerFactory.getLogger(ReferenciaClient.class);

    private final RestClient pacienteClient;
    private final RestClient medicoClient;
    private final boolean validarReferencias;

    public ReferenciaClient(
            @Value("${servicios.paciente.url:http://localhost:8080}") String pacienteUrl,
            @Value("${servicios.medico.url:http://localhost:8081}") String medicoUrl,
            @Value("${servicios.validar-referencias:true}") boolean validarReferencias) {

        this.pacienteClient = RestClient.builder().baseUrl(pacienteUrl).build();
        this.medicoClient = RestClient.builder().baseUrl(medicoUrl).build();
        this.validarReferencias = validarReferencias;
    }

    public void validarPaciente(Long idPaciente) {
        validar("paciente", pacienteClient, "/api/pacientes/" + idPaciente, idPaciente);
    }

    public void validarMedico(Long idMedico) {
        validar("médico", medicoClient, "/api/medicos/" + idMedico, idMedico);
    }

    private void validar(String recurso, RestClient client, String ruta, Long id) {
        if (!validarReferencias) {
            return;
        }

        try {
            client.get().uri(ruta).retrieve().toBodilessEntity();
        } catch (RestClientResponseException ex) {
            if (ex.getStatusCode().value() == 404) {
                throw new ResourceNotFoundException(
                        "El " + recurso + " con id " + id + " no existe");
            }
            log.warn("No se pudo validar el {} con id {}: {}", recurso, id, ex.getMessage());
        } catch (ResourceAccessException ex) {
            log.warn("Servicio de {} no disponible. Se omite la validación del id {}",
                    recurso, id);
        }
    }
}
