-- ============================================================
-- Clinica "Salud Integral" - Microservicios (Semana 11)
-- Crea las bases de datos de desarrollo y produccion.
-- Ejecutar como usuario con privilegios (por ejemplo root):
--   mysql -u root -p < 01_crear_bases_datos.sql
-- ============================================================

-- Ambientes de desarrollo
CREATE DATABASE IF NOT EXISTS bd_paciente_dev CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS bd_medico_dev   CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS bd_citas_dev    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Ambientes de produccion
CREATE DATABASE IF NOT EXISTS bd_paciente CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS bd_medico   CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS bd_citas    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
