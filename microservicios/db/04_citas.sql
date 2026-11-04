-- ============================================================
-- Microservicio de Citas
-- Bases de datos: bd_citas_dev / bd_citas
--
-- En una arquitectura de microservicios cada servicio es dueno de
-- sus datos. Por eso id_paciente e id_medico son referencias
-- logicas (no llaves foraneas hacia otras bases de datos).
-- ============================================================

USE bd_citas_dev;

CREATE TABLE IF NOT EXISTS cita (
    id_cita BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha DATE NOT NULL,
    hora TIME NOT NULL,
    id_paciente BIGINT NOT NULL,
    id_medico BIGINT NOT NULL,
    motivo VARCHAR(300),
    estado VARCHAR(20) NOT NULL DEFAULT 'PROGRAMADA',
    INDEX idx_cita_paciente (id_paciente),
    INDEX idx_cita_medico (id_medico),
    INDEX idx_cita_fecha (fecha)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS atencion (
    id_atencion BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_cita BIGINT NOT NULL UNIQUE,
    diagnostico TEXT,
    tratamiento TEXT,
    observaciones TEXT,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_atencion_cita
        FOREIGN KEY (id_cita) REFERENCES cita (id_cita)
) ENGINE = InnoDB;

USE bd_citas;

CREATE TABLE IF NOT EXISTS cita (
    id_cita BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha DATE NOT NULL,
    hora TIME NOT NULL,
    id_paciente BIGINT NOT NULL,
    id_medico BIGINT NOT NULL,
    motivo VARCHAR(300),
    estado VARCHAR(20) NOT NULL DEFAULT 'PROGRAMADA',
    INDEX idx_cita_paciente (id_paciente),
    INDEX idx_cita_medico (id_medico),
    INDEX idx_cita_fecha (fecha)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS atencion (
    id_atencion BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_cita BIGINT NOT NULL UNIQUE,
    diagnostico TEXT,
    tratamiento TEXT,
    observaciones TEXT,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_atencion_cita
        FOREIGN KEY (id_cita) REFERENCES cita (id_cita)
) ENGINE = InnoDB;
