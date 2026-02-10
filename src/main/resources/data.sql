-- 1 USUARIOS (Credenciales)
INSERT INTO users_security (id,username, email, password, administrador, usuario, invitado, activado) VALUES
    (1,'admin', 'admin@balmis.com', '$2a$10$fzcGgF.8xODz7ptkmZC.OeX1Kj5GDI//FhW2sG0vlshW6ZAKJky0e', true,  true,  false, true), -- password: 5678
    (2,'user',  'user@balmis.com',  '$2a$10$ayw3FCBIkupFt5n9lrmJQe9XZMJhZiNCjaoOkXo/Ba0KZgymO01ce', false, true,  false, true), -- password: 1234
    (3,'guest', 'guest@balmis.com', '$2a$10$ayw3FCBIkupFt5n9lrmJQe9XZMJhZiNCjaoOkXo/Ba0KZgymO01ce', false, false, true,  true); -- password: 1234

-- '$2a$10$n1/e13.wUb0ESrCxjZUNsunO4h/Go9QH1/25co1Scd6DQx1O51/KC' ==>  password: password
-- '$2a$10$APUnUaXbTtPf8AjQqzeHAOTzTw.wFUimrqrSn33dKD6hrO4JR.jcu' ==> password: admin

ALTER TABLE users_security ALTER COLUMN id RESTART WITH (SELECT COALESCE(MAX(id), 0) + 1 FROM users_security);

-- 2 MARCAS (Brands)
INSERT INTO brands(id, name) VALUES
    (1, 'SAMSUNG'),
    (2, 'APPLE'),
    (3, 'XIAOMI'),
    (4, 'GOOGLE');

ALTER TABLE brands ALTER COLUMN id RESTART WITH 5;

-- 3 MÓVILES (Mobiles)
INSERT INTO mobiles (id, model_name, price, ram_gb, storage_gb, description, discount, brand_id) VALUES
    (1, 'Galaxy S24', 950.00, 8, 256, 'Potencia y elegancia con IA', 0, 1),
    (2, 'Galaxy A54', 450.00, 6, 128, 'Gama media premium', 10, 1),
    (3, 'iPhone 15', 1100.00, 6, 128, 'El último modelo de Apple', 0, 2),
    (4, 'iPhone 13', 700.00, 4, 128, 'Modelo anterior a buen precio', 5, 2),
    (5, 'Redmi Note 13', 250.00, 8, 256, 'Calidad precio imbatible', 15, 3),
    (6, 'Pixel 8', 800.00, 8, 128, 'La experiencia Android pura', 0, 4);

ALTER TABLE mobiles ALTER COLUMN id RESTART WITH 7;

-- 4 PEDIDOS (Orders) - Relacionan Usuario y Móvil
-- Usuario con ID 2 ('user') compró varios móviles
INSERT INTO orders (id, user_id, mobile_id, purchase_date) VALUES
    (1, 2, 1, '2025-01-15'), -- User compró Galaxy S24
    (2, 2, 5, '2025-01-20'), -- User compró Redmi Note 13
    (3, 1, 3, '2025-02-01'); -- Admin compró iPhone 15

ALTER TABLE orders ALTER COLUMN id RESTART WITH 4;