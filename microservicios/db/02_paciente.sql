-- ============================================================
-- Microservicio de Pacientes
-- Base de datos: bd_paciente_dev / bd_paciente
-- ============================================================

USE bd_paciente_dev;

CREATE TABLE IF NOT EXISTS paciente (
    id_paciente BIGINT AUTO_INCREMENT PRIMARY KEY,
    dni VARCHAR(8) NOT NULL UNIQUE,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    fecha_nacimiento DATE,
    sexo CHAR(1),
    telefono VARCHAR(15),
    direccion VARCHAR(200),
    correo VARCHAR(100),
    estado CHAR(1) NOT NULL DEFAULT 'A'
) ENGINE = InnoDB;

USE bd_paciente;

CREATE TABLE IF NOT EXISTS paciente (
    id_paciente BIGINT AUTO_INCREMENT PRIMARY KEY,
    dni VARCHAR(8) NOT NULL UNIQUE,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    fecha_nacimiento DATE,
    sexo CHAR(1),
    telefono VARCHAR(15),
    direccion VARCHAR(200),
    correo VARCHAR(100),
    estado CHAR(1) NOT NULL DEFAULT 'A'
) ENGINE = InnoDB;
