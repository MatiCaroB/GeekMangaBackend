CREATE TABLE IF NOT EXISTS categorias (
    categoria_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre_categoria VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS usuarios (
    usuario_id BIGINT AUTO_INCREMENT    PRIMARY KEY,
    nombre                      VARCHAR(100)    NOT NULL
    apellidos                   VARCHAR(100)    ,
    run                         VARCHAR(10)     NOT NULL UNIQUE,
    correo                      VARCHAR(50)     NOT NULL,
    password                    VARCHAR(100)    NOT NULL,
    tipo_usuario                VARCHAR(20)     NOT NULL
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

CREATE TABLE IF NOT EXISTS boletas (
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    fechaEmision            TIMESTAMP,
    total                   INT NOT NULL,
    usuario_id              BIGINT NOT NULL,
    CONSTRAINT fk_boleta_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(usuario_id)
);