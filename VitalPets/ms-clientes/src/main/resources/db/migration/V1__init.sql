CREATE TABLE IF NOT EXISTS clientes (
    id               BIGINT       NOT NULL AUTO_INCREMENT,
    nombre           VARCHAR(255),
    apellido         VARCHAR(255),
    tipo_documento   VARCHAR(50),
    numero_documento VARCHAR(255) UNIQUE,
    telefono         VARCHAR(255),
    email            VARCHAR(255),
    direccion        VARCHAR(255),
    activo           BIT          DEFAULT 1,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS terceros (
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    nombre     VARCHAR(255),
    apellido   VARCHAR(255),
    telefono   VARCHAR(255),
    cliente_id BIGINT       NOT NULL,
    relacion   VARCHAR(255),
    activo     BIT          DEFAULT 1,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
