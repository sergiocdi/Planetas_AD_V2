-- Script para crear el usuario administrador del middleware
-- Este usuario es el único que puede autenticarse y usar la API
-- La contraseña está encriptada con BCrypt

-- Password: middleware_secret_2024 (encriptado con BCrypt)
-- Puedes generar una nueva contraseña con: https://bcrypt-generator.com/

INSERT INTO usuarios (nombre, apellidos, nickname, password, email, monedas)
VALUES (
    'Middleware',
    'Admin',
    'middleware_admin',
    '$2a$10$N9qo8uLOickgx2ZMRZoMy.MqrqQlT0u8Q6J6J6J6J6J6J6J6J6J6J',
    'admin@middleware.system',
    0
);

-- NOTA: Genera tu propia contraseña BCrypt y reemplaza el hash anterior
-- El nickname debe ser exactamente: middleware_admin
