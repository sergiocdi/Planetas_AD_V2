# Guía de Configuración - Versión Middleware

## Descripción del Cambio

Esta versión de la API está diseñada para ser consumida **exclusivamente por un middleware** que gestiona el flujo de las partidas del juego. Los usuarios finales no se conectan directamente a esta API.

## Modelo de Seguridad

### Antes (rama `main`)
```
Usuario Final → API REST → Base de Datos
     │
     └── Cada usuario tiene su propio token JWT
```

### Ahora (rama `feature/middleware-auth`)
```
Usuario Final → Middleware → API REST → Base de Datos
                    │
                    └── Un único token JWT del admin
```

## Cambios Principales

### 1. Autenticación Centralizada
- Solo existe **un usuario administrador** que puede autenticarse
- El nickname del admin es: `middleware_admin`
- El middleware obtiene un token y lo usa para todas las operaciones

### 2. Identificación de Usuarios
- Antes: El usuario se identificaba mediante el token JWT
- Ahora: El middleware envía el `usuarioId` como parámetro en las peticiones

### 3. Endpoints Modificados

| Endpoint | Antes | Ahora |
|----------|-------|-------|
| `POST /api/auth/login` | Cualquier usuario | Solo admin |
| `POST /api/usuarios` | Devolvía token | No devuelve token |
| `POST /api/usuarios/{id}/comprar-planeta` | Validaba que token = id | Solo valida que usuario existe |
| `POST /api/planetas` | Usuario del token | Requiere `usuarioId` en body |
| Todos los endpoints | Algunos públicos | Todos requieren token (excepto login) |

## Configuración Inicial

### 1. Crear el Usuario Administrador

Ejecuta el script SQL en tu base de datos MySQL:

```sql
-- Genera tu propia contraseña BCrypt en: https://bcrypt-generator.com/
INSERT INTO usuarios (nombre, apellidos, nickname, password, email, monedas)
VALUES (
    'Middleware',
    'Admin', 
    'middleware_admin',
    '$2a$10$TU_HASH_BCRYPT_AQUI',
    'admin@middleware.system',
    0
);
```

### 2. Obtener Token desde el Middleware

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"nickname": "middleware_admin", "password": "tu_password"}'
```

Respuesta:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "message": "Middleware authenticated successfully"
}
```

### 3. Usar el Token en Todas las Peticiones

```bash
curl -X GET http://localhost:8080/api/usuarios \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
```

## Ejemplos de Uso

### Crear un Usuario (Jugador)

```bash
curl -X POST http://localhost:8080/api/usuarios \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Juan",
    "apellidos": "García",
    "nickname": "juancho",
    "password": "password123",
    "email": "juan@example.com"
  }'
```

### Comprar un Planeta para un Usuario

```bash
curl -X POST http://localhost:8080/api/usuarios/5/comprar-planeta \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Mi Planeta",
    "tipo": "Fuego"
  }'
```

### Crear Planeta (alternativa)

```bash
curl -X POST http://localhost:8080/api/planetas \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "usuarioId": 5,
    "nombre": "Mi Planeta",
    "tipo": "Agua"
  }'
```

### Guardar una Partida

```bash
curl -X POST http://localhost:8080/api/partidas/guardar \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "partida": {
      "estado": "EN_CURSO",
      "numeroRonda": 1
    },
    "participantes": [
      {"usuario": {"id": 1}, "planeta": {"id": 1}, "vida": 5},
      {"usuario": {"id": 2}, "planeta": {"id": 2}, "vida": 5}
    ]
  }'
```

## Seguridad

### Endpoints Públicos
- `POST /api/auth/login` - Solo para obtener token del admin

### Endpoints Protegidos (requieren token)
- Todos los demás endpoints

### Recomendaciones
1. **Mantén el secreto JWT seguro** - Está en `AuthService.java`
2. **Usa HTTPS en producción** - El token viaja en las cabeceras
3. **Rota la contraseña del admin** periódicamente
4. **Configura el tiempo de expiración** del token según necesidades

## Variables de Configuración

| Variable | Ubicación | Valor |
|----------|-----------|-------|
| Admin nickname | `AuthService.ADMIN_NICKNAME` | `middleware_admin` |
| JWT Secret | `AuthService.key` | Base64 encoded |
| Token expiration | `AuthService.generateToken()` | 3600 segundos (1 hora) |

## Migración desde la Versión Anterior

Si tienes datos de la versión anterior:

1. Los usuarios existentes siguen funcionando como jugadores
2. Necesitas crear el usuario `middleware_admin` nuevo
3. El middleware debe adaptarse para enviar `usuarioId` cuando sea necesario
