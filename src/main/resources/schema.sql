-- Tabla de usuarios
CREATE TABLE IF NOT EXISTS users_security (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL,
    enabled BOOLEAN DEFAULT TRUE
);

-- Tabla 1: Marcas de móviles
CREATE TABLE IF NOT EXISTS brands(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- Tabla 2: Móviles
CREATE TABLE IF NOT EXISTS moviles(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    model_name VARCHAR(100),
    price DOUBLE NOT NULL,
    ram_gb INT NOT NULL,
    storage_gb INT NOT NULL,
    description VARCHAR(200),
    brand_id BIGINT,
    CONSTRAINT fk_brand FOREIGN KEY (brand_id) REFERENCES brands(id) ON DELETE CASCADE
);