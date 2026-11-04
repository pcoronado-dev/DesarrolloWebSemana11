-- ============================================================
-- Datos de prueba para el ambiente de desarrollo
-- Ejecutar despues de 01, 02, 03 y 04.
-- ============================================================

USE bd_paciente_dev;

INSERT IGNORE INTO paciente
    (dni, nombres, apellidos, fecha_nacimiento, sexo, telefono, direccion, correo, estado)
VALUES
    ('12345678', 'Juan', 'Perez Gomez', '1990-05-12', 'M', '987654321',
     'Av. Javier Prado 123, Lima', 'juan.perez@correo.com', 'A'),
    ('87654321', 'Maria', 'Ramirez Torres', '1985-08-23', 'F', '912345678',
     'Calle Los Olivos 456, Lima', 'maria.ramirez@correo.com', 'A');

USE bd_medico_dev;

INSERT IGNORE INTO especialidad (nombre, descripcion, estado) VALUES
    ('Cardiologia', 'Diagnostico y tratamiento del corazon', 'A'),
    ('Medicina general', 'Atencion medica integral', 'A'),
    ('Pediatria', 'Atencion medica de ninos y adolescentes', 'A');

INSERT INTO medico (cmp, nombres, apellidos, telefono, correo, id_especialidad, estado)
SELECT 'CMP12345', 'Ana', 'Torres Ruiz', '999888777', 'ana.torres@clinica.com',
       id_especialidad, 'A'
FROM especialidad
WHERE nombre = 'Cardiologia'
  AND NOT EXISTS (SELECT 1 FROM medico WHERE cmp = 'CMP12345');

INSERT INTO medico (cmp, nombres, apellidos, telefono, correo, id_especialidad, estado)
SELECT 'CMP67890', 'Luis', 'Vargas Mendoza', '988777666', 'luis.vargas@clinica.com',
       id_especialidad, 'A'
FROM especialidad
WHERE nombre = 'Medicina general'
  AND NOT EXISTS (SELECT 1 FROM medico WHERE cmp = 'CMP67890');

INSERT INTO horario_medico (id_medico, dia_semana, hora_inicio, hora_fin)
SELECT m.id_medico, 'LUNES', '09:00:00', '13:00:00'
FROM medico m
WHERE m.cmp = 'CMP12345'
  AND NOT EXISTS (
      SELECT 1 FROM horario_medico h
      WHERE h.id_medico = m.id_medico AND h.dia_semana = 'LUNES'
  );

INSERT INTO horario_medico (id_medico, dia_semana, hora_inicio, hora_fin)
SELECT m.id_medico, 'MIERCOLES', '15:00:00', '19:00:00'
FROM medico m
WHERE m.cmp = 'CMP67890'
  AND NOT EXISTS (
      SELECT 1 FROM horario_medico h
      WHERE h.id_medico = m.id_medico AND h.dia_semana = 'MIERCOLES'
  );

USE bd_citas_dev;

INSERT INTO cita (fecha, hora, id_paciente, id_medico, motivo, estado)
SELECT '2026-11-10', '10:30:00', p.id_paciente, m.id_medico,
       'Control cardiologico', 'PROGRAMADA'
FROM bd_paciente_dev.paciente p
JOIN bd_medico_dev.medico m ON m.cmp = 'CMP12345'
WHERE p.dni = '12345678'
  AND NOT EXISTS (
      SELECT 1 FROM cita c
      WHERE c.fecha = '2026-11-10' AND c.hora = '10:30:00'
        AND c.id_paciente = p.id_paciente AND c.id_medico = m.id_medico
  );

INSERT INTO cita (fecha, hora, id_paciente, id_medico, motivo, estado)
SELECT '2026-11-11', '16:00:00', p.id_paciente, m.id_medico,
       'Consulta general', 'CONFIRMADA'
FROM bd_paciente_dev.paciente p
JOIN bd_medico_dev.medico m ON m.cmp = 'CMP67890'
WHERE p.dni = '87654321'
  AND NOT EXISTS (
      SELECT 1 FROM cita c
      WHERE c.fecha = '2026-11-11' AND c.hora = '16:00:00'
        AND c.id_paciente = p.id_paciente AND c.id_medico = m.id_medico
  );
