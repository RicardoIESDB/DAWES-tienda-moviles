-- Tabla de usuarios
CREATE TABLE IF NOT EXISTS users_security (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    username        VARCHAR(50) NOT NULL,
    email           VARCHAR(100) NOT NULL,
    password        VARCHAR(200) NOT NULL,
    administrador   BOOLEAN NOT NULL,
    usuario         BOOLEAN NOT NULL,
    invitado        BOOLEAN NOT NULL,
    activado        BOOLEAN NOT NULL
);

-- Tabla 1: Marcas de móviles
CREATE TABLE IF NOT EXISTS brands(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- Tabla 2: Móviles
CREATE TABLE IF NOT EXISTS mobiles(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    model_name VARCHAR(100) NOT NULL,
    price DOUBLE NOT NULL,
    ram_gb INT NOT NULL,
    storage_gb INT NOT NULL,
    description VARCHAR(200),
    discount INT(3) NOT NULL DEFAULT 0,
    brand_id BIGINT,
    CONSTRAINT fk_brand FOREIGN KEY (brand_id) REFERENCES brands(id) ON DELETE CASCADE
);

--Tabla de pedidos
CREATE TABLE IF NOT EXISTS orders(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    mobile_id BIGINT NOT NULL,
    purchase_date DATE NOT NULL,
    CONSTRAINT fk_order_user FOREIGN KEY (user_id) REFERENCES users_security(id),
    CONSTRAINT fk_mobile FOREIGN KEY (mobile_id) REFERENCES mobiles(id)
);