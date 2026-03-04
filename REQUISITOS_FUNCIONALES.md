# REQUISITOS FUNCIONALES - Sistema Planetas AD v2

## 1. INFORMACIÓN GENERAL DEL PROYECTO

| Atributo | Descripción |
|----------|-------------|
| **Nombre** | Planetas AD v2 |
| **Tipo** | API REST |
| **Tecnologías** | Spring Boot 4.0.2, Java 20+, MySQL, MongoDB Atlas |
| **Arquitectura** | MVC con capa de servicios |
| **Seguridad** | JWT (JSON Web Token) |

---

## 2. MÓDULOS DEL SISTEMA

### 2.1 Módulo de Autenticación
### 2.2 Módulo de Gestión de Usuarios
### 2.3 Módulo de Gestión de Planetas
### 2.4 Módulo de Gestión de Partidas
### 2.5 Módulo de Estadísticas (MongoDB)

---

## 3. REQUISITOS FUNCIONALES DETALLADOS

---

### RF-01: Registro de Usuario

| Campo | Descripción |
|-------|-------------|
| **ID** | RF-01 |
| **Nombre** | Registro de nuevo usuario |
| **Módulo** | Gestión de Usuarios |
| **Prioridad** | Alta |
| **Actor** | Usuario no autenticado |

**Descripción:**
El sistema debe permitir el registro de nuevos usuarios proporcionando los datos personales requeridos.

**Datos de entrada:**
| Campo | Tipo | Obligatorio | Validación |
|-------|------|-------------|------------|
| nombre | String | Sí | No vacío |
| apellidos | String | Sí | No vacío |
| nickname | String | Sí | Único, no vacío |
| password | String | Sí | No vacío |
| email | String | Sí | Formato email válido, único |

**Datos de salida:**
- Token JWT de autenticación
- ID del usuario creado
- Nickname del usuario
- Monedas iniciales (0 por defecto)
- Lista de planetas (vacía inicialmente)

**Endpoint:** `POST /api/usuarios`

**Reglas de negocio:**
- RN-01.1: La contraseña se almacena encriptada (BCrypt)
- RN-01.2: El nickname debe ser único en el sistema
- RN-01.3: El email debe ser único en el sistema
- RN-01.4: El usuario inicia con 0 monedas

---

### RF-02: Inicio de Sesión (Login)

| Campo | Descripción |
|-------|-------------|
| **ID** | RF-02 |
| **Nombre** | Autenticación de usuario |
| **Módulo** | Autenticación |
| **Prioridad** | Alta |
| **Actor** | Usuario registrado |

**Descripción:**
El sistema debe permitir a los usuarios autenticarse mediante sus credenciales.

**Datos de entrada:**
| Campo | Tipo | Obligatorio |
|-------|------|-------------|
| nickname | String | Sí |
| password | String | Sí |

**Datos de salida:**
- Token JWT válido
- ID del usuario
- Nickname
- Monedas actuales
- Lista de planetas del usuario

**Endpoint:** `POST /api/auth/login`

**Reglas de negocio:**
- RN-02.1: Si las credenciales son inválidas, retornar error 401
- RN-02.2: El token JWT tiene una duración configurable
- RN-02.3: El token contiene el ID del usuario como subject

---

### RF-03: Cierre de Sesión (Logout)

| Campo | Descripción |
|-------|-------------|
| **ID** | RF-03 |
| **Nombre** | Cierre de sesión |
| **Módulo** | Autenticación |
| **Prioridad** | Media |
| **Actor** | Usuario autenticado |

**Descripción:**
El sistema debe permitir a los usuarios cerrar su sesión activa.

**Endpoint:** `POST /api/auth/logout`

**Datos de salida:**
- Mensaje de confirmación: "logged_out"

---

### RF-04: Consultar Usuario por ID

| Campo | Descripción |
|-------|-------------|
| **ID** | RF-04 |
| **Nombre** | Obtener información de usuario |
| **Módulo** | Gestión de Usuarios |
| **Prioridad** | Media |
| **Actor** | Usuario autenticado |

**Descripción:**
El sistema debe permitir consultar la información de un usuario específico.

**Datos de entrada:**
| Campo | Tipo | Obligatorio |
|-------|------|-------------|
| id | Long (path) | Sí |

**Datos de salida:**
- Objeto Usuario completo (excepto password)

**Endpoint:** `GET /api/usuarios/{id}`

**Reglas de negocio:**
- RN-04.1: Si el usuario no existe, retornar error 404

---

### RF-05: Listar Todos los Usuarios

| Campo | Descripción |
|-------|-------------|
| **ID** | RF-05 |
| **Nombre** | Listado de usuarios |
| **Módulo** | Gestión de Usuarios |
| **Prioridad** | Baja |
| **Actor** | Sistema/Admin |

**Descripción:**
El sistema debe permitir obtener el listado completo de usuarios registrados.

**Endpoint:** `GET /api/usuarios`

**Datos de salida:**
- Lista de usuarios (sin passwords)

---

### RF-06: Actualizar Usuario

| Campo | Descripción |
|-------|-------------|
| **ID** | RF-06 |
| **Nombre** | Modificar datos de usuario |
| **Módulo** | Gestión de Usuarios |
| **Prioridad** | Media |
| **Actor** | Usuario autenticado |

**Descripción:**
El sistema debe permitir actualizar los datos de un usuario existente.

**Datos de entrada:**
| Campo | Tipo | Obligatorio |
|-------|------|-------------|
| id | Long (path) | Sí |
| Usuario | Object (body) | Sí |

**Endpoint:** `PUT /api/usuarios/{id}`

---

### RF-07: Eliminar Usuario

| Campo | Descripción |
|-------|-------------|
| **ID** | RF-07 |
| **Nombre** | Eliminar usuario |
| **Módulo** | Gestión de Usuarios |
| **Prioridad** | Baja |
| **Actor** | Usuario autenticado/Admin |

**Descripción:**
El sistema debe permitir eliminar un usuario del sistema.

**Endpoint:** `DELETE /api/usuarios/{id}`

---

### RF-08: Consultar Planetas del Usuario

| Campo | Descripción |
|-------|-------------|
| **ID** | RF-08 |
| **Nombre** | Listar planetas propios |
| **Módulo** | Gestión de Planetas |
| **Prioridad** | Alta |
| **Actor** | Usuario autenticado |

**Descripción:**
El sistema debe permitir a un usuario consultar sus planetas.

**Datos de entrada:**
| Campo | Tipo | Obligatorio |
|-------|------|-------------|
| id | Long (path) | Sí |
| Authorization | Header (Bearer token) | Sí |

**Datos de salida:**
- Lista de planetas del usuario

**Endpoint:** `GET /api/usuarios/{id}/planetas`

**Reglas de negocio:**
- RN-08.1: Requiere token JWT válido

---

### RF-09: Comprar Planeta

| Campo | Descripción |
|-------|-------------|
| **ID** | RF-09 |
| **Nombre** | Compra de nuevo planeta |
| **Módulo** | Gestión de Planetas |
| **Prioridad** | Alta |
| **Actor** | Usuario autenticado |

**Descripción:**
El sistema debe permitir a los usuarios comprar nuevos planetas usando sus monedas.

**Datos de entrada:**
| Campo | Tipo | Obligatorio |
|-------|------|-------------|
| id | Long (path) | Sí |
| nombre | String | Sí |
| tipo | String | Sí |
| Authorization | Header | Sí |

**Datos de salida:**
- Lista actualizada de planetas del usuario

**Endpoint:** `POST /api/usuarios/{id}/comprar-planeta`

**Reglas de negocio:**
- RN-09.1: Solo el propio usuario puede comprar planetas para sí mismo
- RN-09.2: Verificar que el usuario tenga monedas suficientes
- RN-09.3: Costes por tipo de planeta:

| Tipo | Coste (monedas) |
|------|-----------------|
| Normal | 200 |
| Fuego | 300 |
| Agua | 300 |
| Planta | 300 |
| Aire | 350 |
| Roca | 500 |

- RN-09.4: El planeta se crea con 5 vidas iniciales
- RN-09.5: Las monedas se descuentan automáticamente

---

### RF-10: Crear Planeta (Alternativo)

| Campo | Descripción |
|-------|-------------|
| **ID** | RF-10 |
| **Nombre** | Crear planeta directo |
| **Módulo** | Gestión de Planetas |
| **Prioridad** | Media |
| **Actor** | Usuario autenticado |

**Descripción:**
Alternativa para crear planetas directamente desde el endpoint de planetas.

**Endpoint:** `POST /api/planetas`

**Reglas de negocio:**
- Aplican las mismas reglas de RF-09

---

### RF-11: Listar Todos los Planetas

| Campo | Descripción |
|-------|-------------|
| **ID** | RF-11 |
| **Nombre** | Listado global de planetas |
| **Módulo** | Gestión de Planetas |
| **Prioridad** | Media |
| **Actor** | Cualquier usuario |

**Descripción:**
El sistema debe mostrar el listado de todos los planetas del sistema.

**Endpoint:** `GET /api/planetas`

---

### RF-12: Ranking de Planetas

| Campo | Descripción |
|-------|-------------|
| **ID** | RF-12 |
| **Nombre** | Ranking por victorias |
| **Módulo** | Gestión de Planetas |
| **Prioridad** | Alta |
| **Actor** | Cualquier usuario |

**Descripción:**
El sistema debe mostrar un ranking de planetas ordenados por número de victorias.

**Datos de salida:**
| Campo | Tipo |
|-------|------|
| planeta | String (nombre) |
| usuario | String (nickname del dueño) |
| victorias | Integer |

**Endpoint:** `GET /api/planetas/ranking`

---

### RF-13: Actualizar Planeta

| Campo | Descripción |
|-------|-------------|
| **ID** | RF-13 |
| **Nombre** | Modificar planeta |
| **Módulo** | Gestión de Planetas |
| **Prioridad** | Baja |
| **Actor** | Usuario autenticado |

**Endpoint:** `PUT /api/planetas/{id}`

---

### RF-14: Eliminar Planeta

| Campo | Descripción |
|-------|-------------|
| **ID** | RF-14 |
| **Nombre** | Eliminar planeta |
| **Módulo** | Gestión de Planetas |
| **Prioridad** | Baja |
| **Actor** | Usuario autenticado |

**Endpoint:** `DELETE /api/planetas/{id}`

---

### RF-15: Guardar Partida

| Campo | Descripción |
|-------|-------------|
| **ID** | RF-15 |
| **Nombre** | Guardar estado de partida |
| **Módulo** | Gestión de Partidas |
| **Prioridad** | Alta |
| **Actor** | Sistema de juego |

**Descripción:**
El sistema debe permitir guardar el estado de una partida con sus participantes.

**Datos de entrada:**
```json
{
  "partida": {
    "id": null,
    "estado": "EN_CURSO",
    "numeroRonda": 1
  },
  "participantes": [
    {
      "usuario": { "id": 1 },
      "planeta": { "id": 1 },
      "vida": 5
    }
  ]
}
```

**Endpoint:** `POST /api/partidas/guardar`

**Reglas de negocio:**
- RN-15.1: Si la partida ya existe (tiene ID), se actualiza
- RN-15.2: Los participantes se vinculan a la partida

---

### RF-16: Finalizar Partida

| Campo | Descripción |
|-------|-------------|
| **ID** | RF-16 |
| **Nombre** | Finalizar partida simple |
| **Módulo** | Gestión de Partidas |
| **Prioridad** | Alta |
| **Actor** | Sistema de juego |

**Descripción:**
El sistema debe permitir finalizar una partida indicando el ganador.

**Datos de entrada:**
| Campo | Tipo | Obligatorio |
|-------|------|-------------|
| idPartida | Long | Sí |
| jugadores | Array | Sí (primer elemento es el ganador) |

**Endpoint:** `POST /api/partidas/finalizar`

**Reglas de negocio:**
- RN-16.1: El estado de la partida cambia a "FINALIZADO"
- RN-16.2: El ganador recibe 200 monedas
- RN-16.3: Los perdedores reciben 50 monedas
- RN-16.4: Los planetas perdedores pierden 1 vida
- RN-16.5: El planeta ganador incrementa sus victorias

---

### RF-17: Finalizar Partida con Posiciones

| Campo | Descripción |
|-------|-------------|
| **ID** | RF-17 |
| **Nombre** | Finalizar partida con ranking |
| **Módulo** | Gestión de Partidas |
| **Prioridad** | Alta |
| **Actor** | Usuario autenticado |

**Descripción:**
El sistema debe permitir finalizar una partida especificando la posición de cada jugador.

**Datos de entrada:**
```json
{
  "idPartida": 1,
  "jugadores": [
    { "idJugador": 1, "posicion": 1 },
    { "idJugador": 2, "posicion": 2 },
    { "idJugador": 3, "posicion": 3 }
  ]
}
```

**Endpoint:** `POST /api/partidas/finalizar-con-posiciones`

**Reglas de negocio:**
- RN-17.1: Requiere token JWT válido
- RN-17.2: Sistema de premios por posición:
  - 1º puesto: 500 monedas
  - 2º puesto: 450 monedas
  - 3º puesto: 400 monedas
  - Formula: `500 - 50 * (posicion - 1)`
- RN-17.3: Solo el 1º puesto no pierde vida en su planeta
- RN-17.4: El planeta del 1º puesto incrementa victorias

---

### RF-18: Listar Todas las Partidas

| Campo | Descripción |
|-------|-------------|
| **ID** | RF-18 |
| **Nombre** | Listado de partidas |
| **Módulo** | Gestión de Partidas |
| **Prioridad** | Media |
| **Actor** | Cualquier usuario |

**Endpoint:** `GET /api/partidas`

---

### RF-19: Consultar Partida por ID

| Campo | Descripción |
|-------|-------------|
| **ID** | RF-19 |
| **Nombre** | Detalle de partida |
| **Módulo** | Gestión de Partidas |
| **Prioridad** | Media |
| **Actor** | Cualquier usuario |

**Endpoint:** `GET /api/partidas/{id}`

---

### RF-20: Copiar Partidas a MongoDB

| Campo | Descripción |
|-------|-------------|
| **ID** | RF-20 |
| **Nombre** | Sincronización MySQL → MongoDB |
| **Módulo** | Estadísticas |
| **Prioridad** | Alta |
| **Actor** | Administrador/Sistema |

**Descripción:**
El sistema debe permitir copiar todas las partidas almacenadas en MySQL a MongoDB Atlas para análisis estadístico.

**Endpoint:** `POST /api/estadisticas/copiar`

**Datos de salida:**
| Campo | Tipo |
|-------|------|
| mensaje | String |
| partidasCopiadas | Integer |

**Reglas de negocio:**
- RN-20.1: No se duplican partidas ya existentes en MongoDB
- RN-20.2: Se identifica al ganador por la vida > 0 en partidas finalizadas
- RN-20.3: Se almacena información desnormalizada (nickname, nombre planeta, tipo)

---

### RF-21: Listar Partidas de MongoDB

| Campo | Descripción |
|-------|-------------|
| **ID** | RF-21 |
| **Nombre** | Listado estadístico de partidas |
| **Módulo** | Estadísticas |
| **Prioridad** | Media |
| **Actor** | Cualquier usuario |

**Descripción:**
El sistema debe mostrar todas las partidas almacenadas en MongoDB con información completa.

**Endpoint:** `GET /api/estadisticas/partidas`

**Datos de salida:**
- Lista de documentos PartidaDoc con participantes embebidos

---

### RF-22: Usuario con Más Victorias

| Campo | Descripción |
|-------|-------------|
| **ID** | RF-22 |
| **Nombre** | Top usuario ganador |
| **Módulo** | Estadísticas |
| **Prioridad** | Alta |
| **Actor** | Cualquier usuario |

**Descripción:**
El sistema debe identificar y mostrar al usuario que más partidas ha ganado.

**Endpoint:** `GET /api/estadisticas/usuario-top`

**Datos de salida:**
| Campo | Tipo |
|-------|------|
| usuarioId | Long |
| nickname | String |
| partidasGanadas | Long |

---

### RF-23: Tipo de Planeta con Más Victorias

| Campo | Descripción |
|-------|-------------|
| **ID** | RF-23 |
| **Nombre** | Top tipo de planeta |
| **Módulo** | Estadísticas |
| **Prioridad** | Alta |
| **Actor** | Cualquier usuario |

**Descripción:**
El sistema debe identificar qué tipo de planeta ha ganado más partidas.

**Endpoint:** `GET /api/estadisticas/tipo-planeta-top`

**Datos de salida:**
| Campo | Tipo |
|-------|------|
| tipoPlaneta | String |
| partidasGanadas | Long |

---

### RF-24: Ranking de Usuarios por Victorias

| Campo | Descripción |
|-------|-------------|
| **ID** | RF-24 |
| **Nombre** | Ranking completo de usuarios |
| **Módulo** | Estadísticas |
| **Prioridad** | Media |
| **Actor** | Cualquier usuario |

**Descripción:**
El sistema debe mostrar un ranking ordenado de todos los usuarios por victorias.

**Endpoint:** `GET /api/estadisticas/ranking-usuarios`

**Datos de salida:**
- Lista ordenada descendentemente por partidasGanadas

---

### RF-25: Ranking de Tipos de Planeta por Victorias

| Campo | Descripción |
|-------|-------------|
| **ID** | RF-25 |
| **Nombre** | Ranking de tipos de planeta |
| **Módulo** | Estadísticas |
| **Prioridad** | Media |
| **Actor** | Cualquier usuario |

**Descripción:**
El sistema debe mostrar un ranking de tipos de planeta ordenados por victorias.

**Endpoint:** `GET /api/estadisticas/ranking-tipos-planeta`

---

## 4. REQUISITOS NO FUNCIONALES

### RNF-01: Seguridad
- Autenticación mediante JWT
- Contraseñas encriptadas con BCrypt
- CORS habilitado para todos los orígenes
- Sesiones stateless

### RNF-02: Persistencia
- Base de datos principal: MySQL 8.0
- Base de datos de estadísticas: MongoDB Atlas
- ORM: Hibernate/JPA

### RNF-03: Rendimiento
- Conexiones pooled con HikariCP
- Índices en campos de búsqueda frecuente

### RNF-04: Disponibilidad
- Puerto por defecto: 8080
- Despliegue como WAR

---

## 5. MODELO DE DATOS

### 5.1 Entidades MySQL

#### Usuario
| Campo | Tipo | Restricciones |
|-------|------|---------------|
| id | Long | PK, Auto-increment |
| nombre | String | Not null |
| apellidos | String | Not null |
| nickname | String | Unique, Not null |
| password | String | Not null |
| email | String | Unique, Email format |
| monedas | int | Default 0 |

#### Planeta
| Campo | Tipo | Restricciones |
|-------|------|---------------|
| id | Long | PK, Auto-increment |
| nombre | String | |
| tipo | String | (Normal, Fuego, Agua, Planta, Aire, Roca) |
| vidas | int | Default 5 |
| numeroVictorias | int | Default 0 |
| usuario_id | Long | FK → Usuario |

#### Partida
| Campo | Tipo | Restricciones |
|-------|------|---------------|
| id | Long | PK, Auto-increment |
| estado | String | (EN_CURSO, FINALIZADO) |
| numeroRonda | int | |

#### Participante
| Campo | Tipo | Restricciones |
|-------|------|---------------|
| id | Long | PK, Auto-increment |
| partida_id | Long | FK → Partida |
| usuario_id | Long | FK → Usuario |
| planeta_id | Long | FK → Planeta |
| vida | int | |

### 5.2 Documentos MongoDB

#### PartidaDoc
```json
{
  "_id": "ObjectId",
  "partidaIdMysql": "Long",
  "estado": "String",
  "numeroRonda": "int",
  "fechaCreacion": "LocalDateTime",
  "fechaCopiado": "LocalDateTime",
  "participantes": [
    {
      "participanteId": "Long",
      "usuarioId": "Long",
      "usuarioNickname": "String",
      "planetaId": "Long",
      "planetaNombre": "String",
      "planetaTipo": "String",
      "vida": "int",
      "ganador": "boolean"
    }
  ]
}
```

---

## 6. MATRIZ DE TRAZABILIDAD

| Requisito | Controlador | Servicio | Repositorio |
|-----------|-------------|----------|-------------|
| RF-01 | UsuarioController | UsuarioService, AuthService | UsuarioRepository |
| RF-02 | AuthController | AuthService | UsuarioRepository |
| RF-03 | AuthController | - | - |
| RF-04 | UsuarioController | UsuarioService | UsuarioRepository |
| RF-08 | UsuarioController | PlanetaService | PlanetaRepository |
| RF-09 | UsuarioController | PlanetaService | PlanetaRepository, UsuarioRepository |
| RF-12 | PlanetaController | PlanetaService | PlanetaRepository |
| RF-15 | PartidaController | PartidaService | PartidaRepository, ParticipanteRepository |
| RF-16 | PartidaController | PartidaService | Todos |
| RF-17 | PartidaController | PartidaService | Todos |
| RF-20 | EstadisticasController | EstadisticasService | PartidaDocRepository |
| RF-22 | EstadisticasController | EstadisticasService | PartidaDocRepository |
| RF-23 | EstadisticasController | EstadisticasService | PartidaDocRepository |

---

## 7. RESUMEN DE ENDPOINTS API

### Autenticación
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | /api/auth/login | Iniciar sesión |
| POST | /api/auth/logout | Cerrar sesión |

### Usuarios
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | /api/usuarios | Registrar usuario |
| GET | /api/usuarios | Listar usuarios |
| GET | /api/usuarios/{id} | Obtener usuario |
| PUT | /api/usuarios/{id} | Actualizar usuario |
| DELETE | /api/usuarios/{id} | Eliminar usuario |
| GET | /api/usuarios/{id}/planetas | Listar planetas del usuario |
| POST | /api/usuarios/{id}/comprar-planeta | Comprar planeta |

### Planetas
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | /api/planetas | Crear planeta |
| GET | /api/planetas | Listar planetas |
| GET | /api/planetas/ranking | Ranking de planetas |
| PUT | /api/planetas/{id} | Actualizar planeta |
| DELETE | /api/planetas/{id} | Eliminar planeta |

### Partidas
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | /api/partidas/guardar | Guardar partida |
| POST | /api/partidas/finalizar | Finalizar partida |
| POST | /api/partidas/finalizar-con-posiciones | Finalizar con posiciones |
| GET | /api/partidas | Listar partidas |
| GET | /api/partidas/{id} | Obtener partida |

### Estadísticas (MongoDB)
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | /api/estadisticas/copiar | Copiar a MongoDB |
| GET | /api/estadisticas/partidas | Listar partidas MongoDB |
| GET | /api/estadisticas/usuario-top | Usuario top |
| GET | /api/estadisticas/tipo-planeta-top | Tipo planeta top |
| GET | /api/estadisticas/ranking-usuarios | Ranking usuarios |
| GET | /api/estadisticas/ranking-tipos-planeta | Ranking tipos planeta |

---

*Documento generado automáticamente a partir del análisis del código fuente.*
*Fecha: Marzo 2026*
*Versión: 2.0*
