# 🐾 VitalPets — Sistema de Gestión Veterinaria

[![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=java)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.14-brightgreen?style=flat-square&logo=spring)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/Spring%20Security-JWT-brightgreen?style=flat-square&logo=springsecurity)](https://spring.io/projects/spring-security)
[![Spring Cloud Gateway](https://img.shields.io/badge/Spring%20Cloud-Gateway-blue?style=flat-square&logo=spring)](https://spring.io/projects/spring-cloud-gateway)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=flat-square&logo=mysql)](https://www.mysql.com/)
[![Docker](https://img.shields.io/badge/Docker-Compose-blue?style=flat-square&logo=docker)](https://www.docker.com/)
[![Postman](https://img.shields.io/badge/Postman-Tested-orange?style=flat-square&logo=postman)](https://www.postman.com/)

> Sistema de gestión completo para una veterinaria que atiende tanto mascotas de compañía como animales exóticos, basado en una arquitectura de microservicios.

---

## 📖 Contexto del Proyecto

**VitalPets** es un sistema diseñado para responder a las necesidades reales de una veterinaria moderna que atiende desde perros y gatos hasta aves, reptiles y pequeños mamíferos. La aplicación entiende que cada especie tiene sus propios protocolos, herramientas y cuidados, y que detrás de cada mascota hay un dueño legal pero también, muchas veces, un familiar o amigo de confianza que la trae a consulta.

El sistema garantiza la trazabilidad legal de cada visita, permite al personal consultar el inventario de medicamentos en tiempo real, llevar un historial clínico detallado y emitir facturas con el desglose completo de servicios y productos.


---

## 🎯 Requisitos del Sistema

    ### Requisitos Funcionales
    - Gestión de usuarios con roles diferenciados (administrador, trabajador, veterinario)
    - Registro y mantenimiento de mascotas con información detallada por especie
    - Control de inventario de medicamentos y productos con alertas de stock mínimo
    - Módulo de citas con asignación de personal y registro de quien trae a la mascota
    - Historial médico clínico completo por mascota
    - Facturación detallada con desglose de servicios y productos
    - Administración de exámenes de laboratorio
    - Registro de personal con sus especialidades e implementos asignados

### Requisitos No Funcionales
- **Escalabilidad** — cada microservicio puede escalar de forma independiente
- **Disponibilidad** — la caída de un microservicio no detiene al resto
- **Seguridad** — validación de datos, manejo controlado de excepciones
- **Rendimiento** — bases de datos independientes evitan cuellos de botella
- **Mantenibilidad** — separación de responsabilidades por dominio

---

## 🏗️ Arquitectura de Microservicios

El sistema está dividido en **10 microservicios independientes** más un **API Gateway** como punto de entrada único, cada uno con su propia base de datos y comunicándose entre sí a través de servicios REST mediante **WebClient**.

| # | Microservicio | Puerto | Base de datos | Función |
|---|---|---|---|---|
| 0 | **MS-Gateway** | **8080** | — | **Punto de entrada único al sistema. Enruta todas las peticiones a los microservicios correspondientes** |
| 1 | **MS-Mascotas** | 8081 | `mascotas_db` | Registrar las mascotas y sus datos por especie |
| 2 | **MS-Clientes** | 8082 | `clientes_db` | Registra los dueños legales y/o terceros autorizados que llevan una mascota |
| 3 | **MS-Citas** | 8083 | `citas_db` | Agenda las consultas, peluquería y otros servicios |
| 4 | **MS-Historial** | 8084 | `historial_db` | Historial clínico de cada mascota |
| 5 | **MS-Inventario** | 8085 | `inventario_db` | Control de medicamentos, herramientas de trabajo y productos con stock mínimo |
| 6 | **MS-Facturación** | 8086 | `facturacion_db` | Generación de facturas detalladas |
| 7 | **MS-Personal** | 8087 | `personal_db` | Veterinarios, estilistas y técnicos con sus implementos |
| 8 | **MS-Vacunas** | 8088 | `vacunas_db` | Control de vacunas |
| 9 | **MS-Laboratorio** | 8089 | `laboratorio_db` | Solicitudes y carga de resultados de exámenes |
| 10 | **MS-Usuarios** | 8090 | `usuarios_db` | Cuentas de acceso al sistema |

---

## 🌐 API Gateway

El **MS-Gateway** (puerto 8080) es el punto de entrada único al sistema. Todas las peticiones del cliente llegan al Gateway, que las reenvía al microservicio correspondiente según la ruta.

**Tecnología:** Spring Cloud Gateway 2023.0.1 (basado en WebFlux / programación reactiva)

| Ruta Gateway | Microservicio destino | Puerto interno |
|---|---|---|
| `/api/mascotas/**` | MS-Mascotas | 8081 |
| `/api/clientes/**` | MS-Clientes | 8082 |
| `/api/citas/**` | MS-Citas | 8083 |
| `/api/historial/**` | MS-Historial | 8084 |
| `/api/productos/**` | MS-Inventario | 8085 |
| `/api/facturas/**` | MS-Facturación | 8086 |
| `/api/personal/**` | MS-Personal | 8087 |
| `/api/vacunas/**` | MS-Vacunas | 8088 |
| `/api/examenes/**` | MS-Laboratorio | 8089 |
| `/api/usuarios/**` | MS-Usuarios | 8090 |

Con Docker activo, el cliente sólo necesita apuntar a `http://localhost:8080` — el Gateway resuelve el enrutamiento internamente usando los nombres de contenedor.

---

## 🔐 Seguridad — JWT

El sistema utiliza **Spring Security + JWT (JSON Web Token)** para proteger los endpoints de los 10 microservicios.

### Flujo de autenticación

1. El cliente hace **POST `/api/usuarios/login`** con sus credenciales
2. **MS-Usuarios** valida las credenciales y genera un **token JWT firmado**
3. El cliente incluye el token en cada petición posterior como header:
   ```
   Authorization: Bearer <token>
   ```
4. Los **otros 9 microservicios** validan el token en cada request antes de procesar la operación

### Endpoints públicos (sin token)

| Endpoint | Microservicio |
|---|---|
| `POST /api/usuarios/login` | MS-Usuarios |
| `GET /swagger-ui.html` de cada microservicio | Todos |
| `GET /v3/api-docs` de cada microservicio | Todos |

### Ejemplo de uso con Postman

```
# Paso 1 — obtener token
POST http://localhost:8090/api/usuarios/login
Body: { "username": "admin", "password": "admin123" }
→ Respuesta: { "token": "eyJhbGciOiJIUzI1NiJ9..." }

# Paso 2 — usar el token en peticiones protegidas
GET http://localhost:8081/api/mascotas
Headers: Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

---

## 🧪 Pruebas Unitarias

El proyecto implementa **JUnit 5 + Mockito** en los 10 microservicios, cubriendo la capa **Service** (lógica de negocio).

**Patrón de prueba:** `@ExtendWith(MockitoExtension.class)` — los repositorios se mockean con `@Mock` y el servicio bajo prueba se inyecta con `@InjectMocks`.

| Microservicio | Clase testeada | Tests |
|---|---|---|
| MS-Mascotas | `MascotaService` | 11 |
| MS-Clientes | `ClienteService` | 8 |
| MS-Citas | `CitaService` | 8 |
| MS-Historial | `HistorialService` | 7 |
| MS-Inventario | `ProductoService` | 10 |
| MS-Facturación | `FacturaService` | 8 |
| MS-Personal | `PersonalService` | 9 |
| MS-Vacunas | `VacunaService` | 7 |
| MS-Laboratorio | `ExamenService` | 8 |
| MS-Usuarios | `UsuarioService` | 10 |
| **Total** | | **86 tests** |

**Comando para ejecutar los tests de un microservicio:**
```bash
cd ms-mascotas
./mvnw test
```

---

## 🔗 Comunicación entre Microservicios (WebClient)

Uno de los aspectos centrales del proyecto es la **comunicación entre microservicios mediante REST**. Cuando un microservicio necesita verificar información que pertenece a otro dominio, **WebClient** consulta al microservicio responsable. Si la información no existe, la operación se rechaza con un `404 Not Found` antes de que cualquier dato inválido llegue a la base de datos.

| Microservicio origen | Consulta a | Cuándo se realiza |
|---|---|---|
| MS-Citas | MS-Mascotas + MS-Clientes | Al agendar una cita |
| MS-Historial | MS-Mascotas | Al registrar un evento médico |
| MS-Facturación | MS-Citas | Al generar una factura |
| MS-Vacunas | MS-Mascotas | Al registrar una vacuna |
| MS-Laboratorio | MS-Mascotas | Al solicitar un examen |
| MS-Inventario | MS-Personal | Al registrar un producto |
| MS-Personal | MS-Usuarios | Al registrar un trabajador |
| MS-Usuarios | MS-Personal | Al crear una cuenta vinculada a un empleado |

Cada microservicio que usa WebClient tiene la siguiente estructura interna:

```
ms-{nombre}/
├── client/          → clases que llaman a otros microservicios
│   └── XxxClient.java
└── config/          → configuración del WebClient
    └── WebClientConfig.java
```

---

## 👥 Historias de Usuario Implementadas

Cada historia de usuario está vinculada al microservicio (o combinación de ellos) que la hace posible:

| Como... | Quiero... | Implementado en |
|---|---|---|
| 👨‍💼 Administrador | Crear nuevos usuarios para gestionar el acceso al sistema | MS-Usuarios |
| 👨‍⚕️ Trabajador | Crear un perfil por cada mascota que ingrese, registrando su información básica | MS-Mascotas + MS-Clientes |
| 👨‍⚕️ Trabajadores | Registrar medicamentos o productos con stock mínimo definido | MS-Inventario |
| 👨‍⚕️ Trabajador | Registrar la atención completa: medicamento administrado y veterinario que atendió | MS-Historial + MS-Personal |
| 👨‍⚕️ Trabajador | Registrar diagnóstico y tratamiento para seguimiento adecuado | MS-Historial |
| 👨‍⚕️ Trabajador | Generar una factura con detalle de servicios, productos y método de pago | MS-Facturación + MS-Citas |
| 👨‍⚕️ Trabajador | Registrar datos del dueño y de la persona que trae la mascota | MS-Clientes (dueños + terceros autorizados) |
| 👨‍⚕️ Trabajador | Controlar el calendario de vacunación de cada paciente | MS-Vacunas |
| 👨‍⚕️ Trabajador | Solicitar y registrar exámenes de laboratorio | MS-Laboratorio |

---

## 🛠️ Herramientas y Tecnologías

### Lenguajes y frameworks
- **Java 21** — lenguaje principal del backend
- **Spring Boot 3.5.14** — framework para construir cada microservicio
- **Spring Security + JWT** — autenticación y autorización con tokens firmados
- **Spring Cloud Gateway 2023.0.1** — enrutamiento reactivo de peticiones
- **JPA + Hibernate** — capa de persistencia
- **Lombok** — reducción de código repetitivo (getters, setters, builders)
- **Bean Validation** — validación declarativa de campos
- **HTML5 + CSS3** — interfaces web embebidas en cada microservicio

### Herramientas de desarrollo
| Herramienta | Uso en el proyecto |
|---|---|
| **Visual Studio Code** | Editor principal para el desarrollo de los microservicios y frontend HTML |
| **Spring Initializr** | Generación inicial de cada uno de los 10 microservicios con sus dependencias |
| **Docker + Docker Compose** | Contenedor MySQL + 11 servicios dockerizados levantados con un solo comando |
| **Postman** | Pruebas de cada endpoint REST y validación de la comunicación entre microservicios |
| **Maven** | Gestión de dependencias y construcción de cada microservicio |
| **Git + GitHub** | Control de versiones y repositorio remoto |

### Paleta de colores del proyecto
- 🟢 Verde principal: `#78C4A7`
- ⚪ Fondo claro: `#F9FCFB`
- 🟢 Verde suave: `#CEEADF`

---

## 🔄 Evolución del Proyecto: De H2 a Docker

En la fase inicial del desarrollo, cada microservicio utilizaba **H2 Database** en memoria, lo cual permitía levantar y probar los microservicios rápidamente sin configuración adicional. Las pruebas en Postman funcionaban correctamente, pero los datos se perdían al detener cada servicio.

Para acercarse más a un entorno de producción real, se migró toda la persistencia a **MySQL 8.0 ejecutándose en un contenedor Docker**. Esta migración trajo las siguientes ventajas:

| Aspecto | H2 (Al inicio) | MySQL Docker (Ahora) |
|---|---|---|
| Persistencia de datos | ❌ Se pierden al apagar | ✅ Persisten en volumen Docker |
| Aislamiento | ⚠️ En la misma JVM | ✅ Contenedor independiente |
| Realismo del entorno | ⚠️ Solo desarrollo | ✅ Similar a producción |
| Inicialización | Auto al levantar el servicio | Script `init.sql` crea las 10 BD |
| Pruebas con Postman | ✅ Funcionaban | ✅ Funcionan igual |

Las pruebas de Postman **funcionan de la misma manera en ambos casos**, lo cual confirmó que la lógica de negocio era independiente del motor de base de datos.

---

## 🚀 Cómo Ejecutar el Proyecto

### Prerrequisitos
- Docker Desktop instalado y en ejecución
- Postman para probar los endpoints
- Java 21 + Maven (solo si se quiere ejecutar localmente sin Docker)

### Opción A — Levantar todo con Docker (recomendado)

Desde la carpeta `VitalPets/`, ejecutar:

```bash
docker-compose up --build
```

Esto construye y levanta los **11 servicios** en un solo comando:
- 🗄️ MySQL en puerto `3307` (con las 10 bases de datos creadas automáticamente)
- 🌐 API Gateway en `http://localhost:8080` (punto de entrada único)
- ⚙️ 10 microservicios en puertos `8081`–`8090`

Los microservicios esperan a que MySQL esté saludable (`healthcheck`) antes de arrancar.

**Para detener todo:**
```bash
docker-compose down
```

### Opción B — Ejecución local (desarrollo)

**Paso 1 — Levantar solo MySQL con Docker:**
```bash
docker-compose up mysql-vitalpets -d
```

**Paso 2 — Iniciar cada microservicio por separado:**
```bash
cd ms-mascotas
./mvnw spring-boot:run
```
Repetir para cada uno de los 10 microservicios (puertos 8081–8090).

**Paso 3 — Iniciar el Gateway:**
```bash
cd ms-gateway
./mvnw spring-boot:run
```

### Paso final — Probar con Postman
Importar la colección `VitalPets_Postman_v3.json` desde la raíz del repositorio.

Con Docker activo, todas las peticiones van a **`http://localhost:8080`** (Gateway).
En ejecución local, usar el puerto específico de cada microservicio (8081–8090).

---

## 🧪 Pruebas con Postman

La colección **`VitalPets_Postman_v3.json`** incluye:

- ✅ **CRUD completo** para los 10 microservicios
- ✅ **Pruebas del GlobalExceptionHandler** (IDs inexistentes que retornan 404 estructurado)
- ✅ **Pruebas de WebClient válidas** (esperan `201 Created`)
- ✅ **Pruebas de WebClient inválidas** (esperan `404 Not Found`)
- ✅ **Flujo End to End** completo: cliente → mascota → cita → historial → vacuna → factura

### Orden recomendado de ejecución
1. 🔐 Login en MS-Usuarios y copiar el token JWT
2. 🟢 Registrar datos base: cliente, mascota, usuario, personal
3. 🟡 Registrar inventario
4. 🔵 Probar WebClient en cada microservicio (inválido y válido)
5. ⭐ Ejecutar el flujo end-to-end completo

---

## 📁 Estructura del Proyecto

```
VitalPets/
├── docker-compose.yml              ← MySQL + 11 servicios Docker
├── init.sql                        ← Script para crear las 10 BD
├── VitalPets_Postman_v3.json       ← Colección de pruebas Postman
├── README.md                       ← Este archivo
│
├── ms-gateway/                     ← API Gateway (puerto 8080)
│   └── src/main/java/com/vitalpets/gateway/
│       ├── GatewayApplication.java
│       └── config/GatewayConfig.java
│
├── ms-mascotas/                    ← Microservicio 1 (puerto 8081)
│   └── src/main/java/com/vitalpets/mascotas/
│       ├── controller/             ← Endpoints REST
│       ├── service/                ← Lógica de negocio + logs SLF4J
│       ├── repository/             ← JpaRepository
│       ├── model/                  ← Entidades JPA
│       ├── dto/                    ← Objetos de transferencia
│       └── exception/              ← GlobalExceptionHandler
│
├── ms-clientes/                    ← Microservicio 2 (puerto 8082)
├── ms-citas/                       ← Microservicio 3 (puerto 8083)
│   └── src/main/java/com/vitalpets/citas/
│       ├── client/                 ← WebClient para llamar a otros MS
│       └── config/                 ← Configuración del WebClient
│
├── ms-historial/                   ← Microservicio 4 (puerto 8084)
├── ms-inventario/                  ← Microservicio 5 (puerto 8085)
├── ms-facturacion/                 ← Microservicio 6 (puerto 8086)
├── ms-personal/                    ← Microservicio 7 (puerto 8087)
├── ms-vacunas/                     ← Microservicio 8 (puerto 8088)
├── ms-laboratorio/                 ← Microservicio 9 (puerto 8089)
└── ms-usuarios/                    ← Microservicio 10 (puerto 8090)
```

Cada microservicio sigue el **patrón CSR** (Controller → Service → Repository), incluye:
- `@ControllerAdvice` con `GlobalExceptionHandler` para manejo centralizado de errores
- Logs estructurados con SLF4J (`@Slf4j`)
- Respuestas con `ResponseEntity` y códigos HTTP correctos
- DTOs separados de las entidades JPA
- Interfaz web propia en HTML5 + CSS3

---

## 📊 Aspectos Técnicos

- ✅ **Patrón CSR (Controller, Service, Repository)** implementado en los 10 microservicios
- ✅ **Persistencia JPA** con entidades y relaciones correctamente configuradas
- ✅ **Comunicación REST entre microservicios** mediante WebClient
- ✅ **Manejo centralizado de excepciones** con `@ControllerAdvice` en cada servicio
- ✅ **Logs estructurados** con SLF4J en cada Service
- ✅ **Validación de reglas de negocio** con Bean Validation (`@Valid`, `@NotNull`, `@Email`, etc.)
- ✅ **Bases de datos independientes** por microservicio (10 BD en un mismo contenedor MySQL)
- ✅ **DTOs separados** de las entidades para evitar exponer la capa de persistencia
- ✅ **Códigos HTTP correctos** en cada respuesta REST
- ✅ **Migración de BD con Flyway** (`V1__init.sql` por microservicio)
- ✅ **HATEOAS** con enlaces `_links` en todas las respuestas REST
- ✅ **Documentación Swagger UI** en cada microservicio (`/swagger-ui.html`)
- ✅ **Autenticación JWT** con Spring Security (token generado en MS-Usuarios)
- ✅ **Pruebas unitarias JUnit 5 + Mockito** en capa Service (86 tests en total)
- ✅ **API Gateway** con Spring Cloud Gateway (puerto 8080, enrutamiento reactivo)
- ✅ **Sistema completo Dockerizado** (11 servicios + MySQL con healthcheck)

---

## 📝 Historial de Versiones

| Versión | Evaluación | Cambios implementados |
|---|---|---|
| v1.0.0 | Parcial 2 | 10 microservicios con patrón CSR, CRUD completo, GlobalExceptionHandler, logs SLF4J, DTOs, WebClient entre microservicios, interfaz HTML5/CSS3 |
| v2.0.0 | Parcial 3 | Flyway (migraciones SQL), HATEOAS (_links en respuestas), Testing JUnit 5 + Mockito (86 tests), Swagger UI por microservicio, JWT con Spring Security, Docker Compose completo, API Gateway (puerto 8080) |

> El proyecto creció de **10 microservicios** con funcionalidad básica
> a un **sistema distribuido completo** con seguridad, documentación,
> pruebas automatizadas y despliegue containerizado.

---

## 👨‍💻 Autor

**Leandro Ruiz**  
Estudiante de Ingeniería Informática — Duoc UC  
Desarrollo individual del proyecto.

---

## 📚 Asignatura

**DSY1103 — Desarrollo FullStack I**  
**Evaluación Parcial 3** — Arquitectura de Microservicios  
**Duoc UC** — 2026
