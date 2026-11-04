-- ============================================================
-- Microservicio de Medicos
-- Bases de datos: bd_medico_dev / bd_medico
-- ============================================================

USE bd_medico_dev;

CREATE TABLE IF NOT EXISTS especialidad (
    id_especialidad BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(200),
    estado CHAR(1) NOT NULL DEFAULT 'A'
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS medico (
    id_medico BIGINT AUTO_INCREMENT PRIMARY KEY,
    cmp VARCHAR(20) NOT NULL UNIQUE,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    telefono VARCHAR(15),
    correo VARCHAR(100),
    id_especialidad BIGINT NOT NULL,
    estado CHAR(1) NOT NULL DEFAULT 'A',
    CONSTRAINT fk_medico_especialidad
        FOREIGN KEY (id_especialidad) REFERENCES especialidad (id_especialidad)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS horario_medico (
    id_horario BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_medico BIGINT NOT NULL,
    dia_semana VARCHAR(15),
    hora_inicio TIME,
    hora_fin TIME,
    CONSTRAINT fk_horario_medico
        FOREIGN KEY (id_medico) REFERENCES medico (id_medico)
) ENGINE = InnoDB;

USE bd_medico;

CREATE TABLE IF NOT EXISTS especialidad (
    id_especialidad BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(200),
    estado CHAR(1) NOT NULL DEFAULT 'A'
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS medico (
    id_medico BIGINT AUTO_INCREMENT PRIMARY KEY,
    cmp VARCHAR(20) NOT NULL UNIQUE,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    telefono VARCHAR(15),
    correo VARCHAR(100),
    id_especialidad BIGINT NOT NULL,
    estado CHAR(1) NOT NULL DEFAULT 'A',
    CONSTRAINT fk_medico_especialidad
        FOREIGN KEY (id_especialidad) REFERENCES especialidad (id_especialidad)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS horario_medico (
    id_horario BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_medico BIGINT NOT NULL,
    dia_semana VARCHAR(15),
    hora_inicio TIME,
    hora_fin TIME,
    CONSTRAINT fk_horario_medico
        FOREIGN KEY (id_medico) REFERENCES medico (id_medico)
) ENGINE = InnoDB;
