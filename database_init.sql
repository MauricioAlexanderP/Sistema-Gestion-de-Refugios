-- Script de inicialización para autenticación JWT
-- Base de datos: refugio_animal

USE refugio_animal;

-- Insertar roles básicos (si no existen)
INSERT IGNORE INTO
    roles (
        id,
        nombre,
        descripcion,
        estado_registro
    )
VALUES (
        1,
        'ADMIN',
        'Administrador del sistema con acceso completo',
        true
    ),
    (
        2,
        'GERENTE',
        'Gerente de refugio con acceso limitado',
        true
    );

-- Insertar usuario administrador por defecto
-- Contraseña: admin123 (hash BCrypt)
INSERT IGNORE INTO
    usuarios (
        id,
        nombre,
        usuario,
        contrasena,
        email,
        rol_id,
        estado_registro
    )
VALUES (
        1,
        'Administrador Principal',
        'admin',
        '$2a$10$bqgOg0/8moypu2KxU6VIrO/itXiYz/Yscv6KWvURpow.sblOpOtG2',
        'admin@refugio.com',
        1,
        true
    );

-- Insertar usuario gerente de prueba
-- Contraseña: gerente123 (hash BCrypt)
INSERT IGNORE INTO
    usuarios (
        id,
        nombre,
        usuario,
        contrasena,
        email,
        rol_id,
        estado_registro
    )
VALUES (
        2,
        'Gerente de Prueba',
        'gerente',
        '$2a$10$cjqxksMG2g9DXyD272ZlleqnWpnVBYn3nDUSZfqrcgIfACQxyuL2e',
        'gerente@refugio.com',
        2,
        true
    );

-- Verificar que los usuarios fueron creados correctamente
SELECT u.id, u.nombre, u.usuario, u.email, r.nombre as rol, u.estado_registro
FROM usuarios u
    INNER JOIN roles r ON u.rol_id = r.id
WHERE
    u.id IN (1, 2);

-- Notas:
-- 1. Los hashes de contraseña mostrados son ejemplos. En la práctica,
--    estas deberían ser generadas por BCrypt con las contraseñas reales.
-- 2. Para generar el hash real de 'admin123', usar BCryptPasswordEncoder en Java.
-- 3. Para generar el hash real de 'gerente123', usar BCryptPasswordEncoder en Java.

-- Script para generar hash BCrypt en Java:
-- BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
-- String hash = encoder.encode("admin123");
-- System.out.println(hash);