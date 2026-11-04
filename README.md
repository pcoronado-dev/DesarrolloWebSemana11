# Semana 11 - Desarrollo de Aplicaciones Web

Laboratorio **GLAB-S11: Creación de Microservicios**.

El proyecto de la clínica **"Salud Integral"** se encuentra en la carpeta
[`microservicios`](microservicios/README.md) e incluye:

- `paciente-service` (puerto 8080)
- `medico-service` (puerto 8081)
- `citas-service` (puerto 8082)
- Scripts SQL para las bases de datos `dev` y `prod`
- Colección Postman con las pruebas de todos los endpoints

## Inicio rápido

```powershell
cd microservicios\paciente-service
.\mvnw.cmd spring-boot:run
```

Repite el proceso en `medico-service` y `citas-service`. La configuración de variables
de entorno, endpoints y ejemplos está documentada en
[`microservicios/README.md`](microservicios/README.md).

## Documento del laboratorio

- [`GLAB-S11-RCOELLO-2026-01-3.md`](GLAB-S11-RCOELLO-2026-01-3.md)
