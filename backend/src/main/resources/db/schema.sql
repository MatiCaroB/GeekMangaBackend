-- =====================================================================
-- Modelo de datos de GeekManga (MySQL)
-- Referencia/documentación: Hibernate crea y actualiza estas tablas
-- automáticamente (spring.jpa.hibernate.ddl-auto=update).
-- Si se quiere crear la base a mano, se puede ejecutar este script.
-- =====================================================================

CREATE DATABASE IF NOT EXISTS GeekManga;
USE GeekManga;

CREATE TABLE IF NOT EXISTS categorias (
    categoria_id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre_categoria    VARCHAR(50)  NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS usuarios (
    usuario_id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre          VARCHAR(25)  NOT NULL,
    apellidos       VARCHAR(30),
    run             VARCHAR(10)  NOT NULL UNIQUE,
    correo          VARCHAR(100) NOT NULL UNIQUE,
    password        VARCHAR(255) NOT NULL,           -- hash BCrypt
    tipo_usuario    VARCHAR(20)  NOT NULL            -- ADMIN | VENDEDOR | USER
);

CREATE TABLE IF NOT EXISTS productos (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo          VARCHAR(50)  NOT NULL UNIQUE,
    producto        VARCHAR(50)  NOT NULL,
    descripcion     VARCHAR(300),
    marca           VARCHAR(255) NOT NULL,
    precio          INT          NOT NULL,
    precio_oferta   INT,
    stock           INT          NOT NULL,
    stock_critico   INT          NOT NULL,
    oferta          BOOLEAN      NOT NULL DEFAULT FALSE,
    url_foto        VARCHAR(255),
    categoria_id    BIGINT       NOT NULL,
    CONSTRAINT fk_producto_categoria FOREIGN KEY (categoria_id) REFERENCES categorias (categoria_id)
);

CREATE TABLE IF NOT EXISTS boletas (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha_emision   DATETIME(6)  NOT NULL,
    total           INT          NOT NULL,
    usuario_id      BIGINT       NOT NULL,
    CONSTRAINT fk_boleta_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios (usuario_id)
);

CREATE TABLE IF NOT EXISTS detalle_boleta (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    cantidad         INT    NOT NULL,
    precio_unitario  INT    NOT NULL,
    subtotal         INT    NOT NULL,
    boleta_id        BIGINT NOT NULL,
    producto_id      BIGINT NOT NULL,
    CONSTRAINT fk_detalle_boleta   FOREIGN KEY (boleta_id)   REFERENCES boletas (id),
    CONSTRAINT fk_detalle_producto FOREIGN KEY (producto_id) REFERENCES productos (id)
);
