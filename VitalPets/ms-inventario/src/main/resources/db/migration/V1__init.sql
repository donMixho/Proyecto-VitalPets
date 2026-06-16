CREATE TABLE IF NOT EXISTS productos (
    id              BIGINT       NOT NULL AUTO_INCREMENT,
    nombre          VARCHAR(255) NOT NULL,
    descripcion     VARCHAR(255),
    categoria       VARCHAR(50)  NOT NULL,
    stock_actual    INT          NOT NULL,
    stock_minimo    INT          NOT NULL,
    precio_unitario DOUBLE       NOT NULL,
    unidad_medida   VARCHAR(255),
    activo          BIT          DEFAULT 1,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
