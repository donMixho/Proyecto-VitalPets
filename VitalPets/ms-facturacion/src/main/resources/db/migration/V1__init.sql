CREATE TABLE IF NOT EXISTS facturas (
    id                 BIGINT       NOT NULL AUTO_INCREMENT,
    cita_id            BIGINT       NOT NULL,
    cliente_id         BIGINT       NOT NULL,
    mascota_id         BIGINT       NOT NULL,
    personal_id        BIGINT       NOT NULL,
    nombre_quien_trae  VARCHAR(255),
    fecha_emision      DATETIME(6),
    estado             VARCHAR(50)  DEFAULT 'PENDIENTE',
    metodo_pago        VARCHAR(50)  DEFAULT 'PENDIENTE',
    total_servicios    DOUBLE,
    total_productos    DOUBLE,
    total_final        DOUBLE,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS detalles_factura (
    id              BIGINT       NOT NULL AUTO_INCREMENT,
    factura_id      BIGINT       NOT NULL,
    descripcion     VARCHAR(255),
    tipo_item       VARCHAR(255),
    cantidad        INT,
    precio_unitario DOUBLE,
    subtotal        DOUBLE,
    PRIMARY KEY (id),
    CONSTRAINT fk_detalle_factura FOREIGN KEY (factura_id) REFERENCES facturas (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
