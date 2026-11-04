# Clinica "Salud Integral" - Microservicios (Semana 11)

Implementacion de la actividad del laboratorio **GLAB-S11: Creacion de Microservicios**.
El sistema esta compuesto por tres microservicios Spring Boot independientes, cada uno con
su propia base de datos, su propio puerto y su propio ciclo de despliegue.

| Microservicio | Puerto DEV | Puerto PROD | Base de datos DEV | Base de datos PROD |
| --- | ---: | ---: | --- | --- |
| `paciente-service` | 8080 | 9080 | `bd_paciente_dev` | `bd_paciente` |
| `medico-service` | 8081 | 9081 | `bd_medico_dev` | `bd_medico` |
| `citas-service` | 8082 | 9082 | `bd_citas_dev` | `bd_citas` |

## Arquitectura

```text
                         +----------------------+
                         |   Postman / Frontend |
                         +----------+-----------+
                                    |
        +---------------------------+---------------------------+
        |                           |                           |
        v                           v                           v
+----------------+         +----------------+         +----------------+
| paciente-service|        | medico-service |         | citas-service  |
|   :8080         |        |   :8081        |         |   :8082        |
|                 |        |                |         |                |
| Paciente        |        | Especialidad   |         | Cita           |
|                 |        | Medico         |         | Atencion       |
|                 |        | HorarioMedico  |         |                |
+--------+--------+         +--------+-------+         +--------+-------+
         |                           |                          |
         v                           v                          v
   bd_paciente_dev            bd_medico_dev              bd_citas_dev
```

`citas-service` valida que el paciente y el medico existan mediante llamadas HTTP
(`RestClient`) a los otros dos servicios. Si un servicio no esta disponible, la validacion
se omite y se registra una advertencia, de modo que una caida no bloquea todo el sistema.
La validacion se puede desactivar con `VALIDAR_REFERENCIAS=false`.

Cada servicio es dueno de sus datos. Por eso `cita.id_paciente` y `cita.id_medico` son
referencias logicas, no llaves foraneas entre bases de datos. Las relaciones que si viven
en la misma base (`medico -> especialidad`, `horario_medico -> medico`, `atencion -> cita`)
se mantienen como llaves foraneas.

## Tecnologias

- Java 25 (probado con JDK 26 compilando a `release 25`)
- Spring Boot 4.1.0
- Spring Web MVC, Spring Data JPA, Validation, DevTools
- MySQL 8 (driver `mysql-connector-j`)
- Maven Wrapper incluido (`mvnw` / `mvnw.cmd`)
- H2 solo para pruebas automatizadas

## Estructura del proyecto

```text
microservicios/
|-- paciente-service/
|-- medico-service/
|-- citas-service/
|-- db/
|   |-- 01_crear_bases_datos.sql
|   |-- 02_paciente.sql
|   |-- 03_medico.sql
|   |-- 04_citas.sql
|   `-- 05_datos_prueba.sql
|-- postman/
|   `-- SaludIntegral-S11.postman_collection.json
`-- README.md
```

Cada servicio sigue la estructura solicitada:

```text
src/main/java/com/tecsup/
|-- controller/
|-- model/
|-- dto/
|-- exception/
|-- repository/
`-- services/
```

## Puesta en marcha

### 1. Crear las bases de datos

Con MySQL en ejecucion:

```powershell
cd microservicios\db
mysql -u root -p < 01_crear_bases_datos.sql
mysql -u root -p < 02_paciente.sql
mysql -u root -p < 03_medico.sql
mysql -u root -p < 04_citas.sql
mysql -u root -p < 05_datos_prueba.sql   # opcional
```

Si usas un cliente grafico (MySQL Workbench, DBeaver, HeidiSQL), abre y ejecuta los
scripts en el mismo orden.

> En `dev` Hibernate crea/actualiza las tablas automaticamente (`ddl-auto=update`),
> por lo que basta con crear las bases de datos vacias. Los scripts completos son
> necesarios para `prod`, donde se usa `ddl-auto=validate`.

### 2. Configurar las variables de entorno

Cada servicio lee las mismas variables, pero con valores distintos segun el servicio.
En IntelliJ: **Run > Edit Configurations > Environment variables**.

| Variable | Descripcion | Ejemplo |
| --- | --- | --- |
| `PROFILE_ACTIVE` | Perfil activo | `dev` |
| `SERVER_PORT` | Puerto del servicio | `8080` |
| `DB_URL` | URL JDBC de su base de datos | `jdbc:mysql://localhost:3306/bd_paciente_dev?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true` |
| `DB_USER` | Usuario de MySQL | `root` |
| `DB_PASSWORD` | Contrasena de MySQL | `tu_password` |

Valores por servicio:

```text
paciente-service -> SERVER_PORT=8080, DB_URL=...bd_paciente_dev...
medico-service   -> SERVER_PORT=8081, DB_URL=...bd_medico_dev...
citas-service    -> SERVER_PORT=8082, DB_URL=...bd_citas_dev...
```

`citas-service` tambien acepta:

| Variable | Valor por defecto |
| --- | --- |
| `PACIENTE_URL` | `http://localhost:8080` |
| `MEDICO_URL` | `http://localhost:8081` |
| `VALIDAR_REFERENCIAS` | `true` |

### 3. Ejecutar los servicios

Desde tres terminales distintas:

```powershell
cd microservicios\paciente-service
.\mvnw.cmd spring-boot:run
```

```powershell
cd microservicios\medico-service
.\mvnw.cmd spring-boot:run
```

```powershell
cd microservicios\citas-service
.\mvnw.cmd spring-boot:run
```

Tambien puedes ejecutar cada clase `*Application` desde IntelliJ.

Para el perfil de produccion, cambia `PROFILE_ACTIVE=prod` y usa los puertos y bases
de datos de produccion. Recuerda ejecutar antes los scripts de creacion de tablas.

### 4. Pruebas automatizadas

```powershell
cd microservicios\paciente-service
.\mvnw.cmd test
```

```powershell
cd microservicios\medico-service
.\mvnw.cmd test
```

```powershell
cd microservicios\citas-service
.\mvnw.cmd test
```

Las pruebas usan el perfil `test` con H2 en memoria, por lo que no necesitan MySQL.

## Endpoints

### paciente-service (`/api/pacientes`)

| Metodo | Endpoint | Descripcion |
| --- | --- | --- |
| GET | `/api/pacientes` | Lista pacientes. Acepta `?estado=A` |
| GET | `/api/pacientes/{id}` | Obtiene un paciente |
| POST | `/api/pacientes` | Registra un paciente |
| PUT | `/api/pacientes/{id}` | Actualiza un paciente |
| DELETE | `/api/pacientes/{id}` | Elimina un paciente |

### medico-service

| Metodo | Endpoint | Descripcion |
| --- | --- | --- |
| GET | `/api/especialidades` | Lista especialidades |
| GET | `/api/especialidades/{id}` | Obtiene una especialidad |
| POST | `/api/especialidades` | Registra una especialidad |
| PUT | `/api/especialidades/{id}` | Actualiza una especialidad |
| DELETE | `/api/especialidades/{id}` | Elimina una especialidad |
| GET | `/api/medicos` | Lista medicos. Acepta `?idEspecialidad=` o `?estado=` |
| GET | `/api/medicos/{id}` | Obtiene un medico |
| POST | `/api/medicos` | Registra un medico |
| PUT | `/api/medicos/{id}` | Actualiza un medico |
| DELETE | `/api/medicos/{id}` | Elimina un medico |
| GET | `/api/horarios` | Lista horarios. Acepta `?idMedico=` |
| GET | `/api/horarios/{id}` | Obtiene un horario |
| POST | `/api/horarios` | Registra un horario |
| PUT | `/api/horarios/{id}` | Actualiza un horario |
| DELETE | `/api/horarios/{id}` | Elimina un horario |

### citas-service

| Metodo | Endpoint | Descripcion |
| --- | --- | --- |
| GET | `/api/citas` | Lista citas. Acepta `?idPaciente=`, `?idMedico=`, `?fecha=`, `?estado=` |
| GET | `/api/citas/{id}` | Obtiene una cita |
| POST | `/api/citas` | Registra una cita (estado inicial `PROGRAMADA`) |
| PUT | `/api/citas/{id}` | Actualiza fecha, hora, paciente, medico y motivo |
| PATCH | `/api/citas/{id}/estado` | Cambia el estado de la cita |
| DELETE | `/api/citas/{id}` | Cancela la cita (estado `CANCELADA`) |
| GET | `/api/atenciones` | Lista atenciones |
| GET | `/api/atenciones/{id}` | Obtiene una atencion |
| GET | `/api/atenciones/cita/{idCita}` | Obtiene la atencion de una cita |
| POST | `/api/atenciones` | Registra la atencion y marca la cita como `ATENDIDA` |
| PUT | `/api/atenciones/{id}` | Actualiza una atencion |
| DELETE | `/api/atenciones/{id}` | Elimina una atencion |

Estados validos de una cita: `PROGRAMADA`, `CONFIRMADA`, `ATENDIDA`, `CANCELADA`.

## Ejemplos de peticiones

Crear paciente:

```json
{
  "dni": "12345678",
  "nombres": "Juan",
  "apellidos": "Perez Gomez",
  "fechaNacimiento": "1990-05-12",
  "sexo": "M",
  "telefono": "987654321",
  "direccion": "Av. Javier Prado 123, Lima",
  "correo": "juan.perez@correo.com",
  "estado": "A"
}
```

Crear especialidad:

```json
{
  "nombre": "Cardiologia",
  "descripcion": "Diagnostico y tratamiento del corazon",
  "estado": "A"
}
```

Crear medico:

```json
{
  "cmp": "CMP12345",
  "nombres": "Ana",
  "apellidos": "Torres Ruiz",
  "telefono": "999888777",
  "correo": "ana.torres@clinica.com",
  "idEspecialidad": 1,
  "estado": "A"
}
```

Crear horario:

```json
{
  "idMedico": 1,
  "diaSemana": "LUNES",
  "horaInicio": "09:00:00",
  "horaFin": "13:00:00"
}
```

Crear cita:

```json
{
  "fecha": "2026-11-10",
  "hora": "10:30:00",
  "idPaciente": 1,
  "idMedico": 1,
  "motivo": "Control cardiologico"
}
```

Cambiar estado:

```json
{
  "estado": "CONFIRMADA"
}
```

Registrar atencion:

```json
{
  "idCita": 1,
  "diagnostico": "Hipertension arterial controlada",
  "tratamiento": "Losartan 50 mg cada 24 horas",
  "observaciones": "Control en 30 dias"
}
```

## Manejo de errores

Todos los servicios devuelven un JSON uniforme:

```json
{
  "timestamp": "2026-11-03T21:00:00",
  "status": 400,
  "error": "Bad Request",
  "mensaje": "Datos invalidos",
  "ruta": "/api/pacientes",
  "errores": {
    "dni": "El DNI debe tener exactamente 8 digitos"
  }
}
```

| Codigo | Situacion |
| --- | --- |
| 400 | Datos invalidos, JSON mal formado o regla de negocio incumplida |
| 404 | Recurso no encontrado |
| 409 | DNI/CMP duplicado, atencion duplicada o restriccion de integridad |

## Coleccion Postman

Importa `postman/SaludIntegral-S11.postman_collection.json`. La coleccion guarda
automaticamente los ids creados en variables (`pacienteId`, `medicoId`, `citaId`, etc.)
para que puedas ejecutar las peticiones en orden.

## Evidencias sugeridas para el informe

1. Variables de entorno configuradas en IntelliJ.
2. Ejecucion de los tres servicios en `dev` (puertos 8080, 8081 y 8082).
3. Pruebas Postman del CRUD de pacientes.
4. Pruebas Postman del CRUD de medicos y horarios.
5. Pruebas Postman del CRUD de citas.
6. Cambio de estado de una cita y cancelacion.
7. Registro de atencion y verificacion del estado `ATENDIDA`.
8. Ejecucion con `PROFILE_ACTIVE=prod` y evidencia de las bases de datos de produccion.

## Notas y soluciones frecuentes

- **`Access denied for user 'root'@'localhost'`**: la contrasena de MySQL no coincide.
  Ajusta `DB_PASSWORD` en cada configuracion de ejecucion.
- **`Public Key Retrieval is not allowed`**: agrega
  `allowPublicKeyRetrieval=true` a la URL JDBC (ya esta incluido por defecto).
- **Puerto ocupado**: cambia `SERVER_PORT` o detiene el proceso que usa el puerto.
- **`ddl-auto=validate` falla en prod**: ejecuta los scripts `02`, `03` y `04` antes de
  iniciar los servicios, o cambia temporalmente a `update`.
- **citas-service no valida paciente/medico**: verifica que `paciente-service` y
  `medico-service` esten levantados y que `PACIENTE_URL` / `MEDICO_URL` sean correctos.
  Tambien puedes usar `VALIDAR_REFERENCIAS=false` para pruebas aisladas.
