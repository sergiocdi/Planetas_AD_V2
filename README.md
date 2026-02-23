# Planetas AD v2 - API REST

Este proyecto es una API REST desarrollada con Spring Boot para la gestión de un juego de planetas, usuarios y partidas.

## 🔐 Autenticación
La mayoría de los endpoints protegidos requieren un token JWT. Debes incluirlo en la cabecera de tus peticiones:
`Authorization: Bearer <tu_token>`

---

## 📡 Documentación de Endpoints

### 1. Autenticación (`/api/auth`)

#### **Login**
*   **URL:** `POST /api/auth/login`
*   **Cuerpo (JSON):**
    ```json
    {
      "nickname": "usuario123",
      "password": "mi_password"
    }
    ```
*   **Respuesta Exitosa (200 OK):**
    ```json
    {
      "token": "eyJhbG...",
      "id": 1,
      "nickname": "usuario123",
      "monedas": 100,
      "planetas": [...]
    }
    ```

---

### 2. Usuarios (`/api/usuarios`)

#### **Registro de Usuario**
*   **URL:** `POST /api/usuarios`
*   **Cuerpo (JSON):**
    ```json
    {
      "nombre": "Juan",
      "apellidos": "Pérez",
      "nickname": "juanp",
      "password": "password123",
      "email": "juan@example.com"
    }
    ```
*   **Respuesta:** Devuelve el usuario creado junto con su token JWT inicial.

#### **Listar Todos los Usuarios**
*   **URL:** `GET /api/usuarios`
*   **Acceso:** Público (o según configuración).

#### **Obtener Usuario por ID**
*   **URL:** `GET /api/usuarios/{id}`

#### **Comprar un Planeta (Requiere Token)**
*   **URL:** `POST /api/usuarios/{id}/comprar-planeta`
*   **Cuerpo (JSON):**
    ```json
    {
      "nombre": "Marte X",
      "tipo": "Fuego"
    }
    ```
*   **Respuesta:** Devuelve la lista actualizada de planetas del usuario.

---

### 3. Planetas (`/api/planetas`)

#### **Listar Todos los Planetas**
*   **URL:** `GET /api/planetas`

#### **Ranking de Planetas**
*   **URL:** `GET /api/planetas/ranking`
*   **Respuesta:**
    ```json
    [
      {
        "planeta": "Tierra",
        "usuario": "admin",
        "victorias": 10
      }
    ]
    ```

#### **Crear Planeta (Requiere Token)**
*   **URL:** `POST /api/planetas`
*   **Cuerpo (JSON):**
    ```json
    {
      "nombre": "Nuevo Planeta",
      "tipo": "Agua",
      "vidas": 5
    }
    ```

---

### 4. Partidas (`/api/partidas`)

#### **Guardar Partida**
*   **URL:** `POST /api/partidas/guardar`
*   **Cuerpo (JSON):**
    ```json
    {
      "partida": {
        "estado": "en_progreso",
        "numeroRonda": 1
      },
      "participantes": [
        {
          "usuario": { "id": 1 },
          "planeta": { "id": 5 },
          "vida": 100
        }
      ]
    }
    ```

#### **Finalizar Partida Simple**
*   **URL:** `POST /api/partidas/finalizar`
*   **Cuerpo (JSON):**
    ```json
    {
      "idPartida": 10,
      "jugadores": [
        { "idJugador": 1 }
      ]
    }
    ```

#### **Finalizar con Posiciones (Requiere Token)**
*   **URL:** `POST /api/partidas/finalizar-con-posiciones`
*   **Cuerpo (JSON):**
    ```json
    {
      "idPartida": 10,
      "jugadores": [
        { "idJugador": 1, "posicion": 1 },
        { "idJugador": 2, "posicion": 2 }
      ]
    }
    ```

---

## 🛠️ Desarrollo y Despliegue

### Requisitos
*   Java 21
*   Maven
*   MySQL

### Construcción
Para generar el archivo WAR listo para Azure:
```bash
./mvnw clean package -DskipTests
```
El archivo se generará en `target/Planetas_AD_v2-0.0.1-SNAPSHOT.war`.
