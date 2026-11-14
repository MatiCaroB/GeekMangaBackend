CREATE TABLE IF NOT EXISTS categorias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre_categoria VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS productos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    producto VARCHAR(100) NOT NULL,
    descripcion TEXT,
    marca VARCHAR(100),
    precio INT NOT NULL,
    precio_oferta INT,
    stock INT NOT NULL,
    stock_critico INT,
    oferta BOOLEAN NOT NULL DEFAULT FALSE,
    url_foto VARCHAR(255),
    categoria_id BIGINT NOT NULL,
    CONSTRAINT fk_producto_categoria FOREIGN KEY (categoria_id) REFERENCES categorias(id)
);