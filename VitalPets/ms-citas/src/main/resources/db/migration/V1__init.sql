CREATE TABLE IF NOT EXISTS citas (
    id                 BIGINT       NOT NULL AUTO_INCREMENT,
    mascota_id         BIGINT       NOT NULL,
    cliente_id         BIGINT       NOT NULL,
    tercero_id         BIGINT,
    personal_id        BIGINT       NOT NULL,
    fecha_hora         DATETIME(6)  NOT NULL,
    tipo_servicio      VARCHAR(50)  NOT NULL,
    estado             VARCHAR(50)  DEFAULT 'PROGRAMADA',
    observaciones      VARCHAR(500),
    nombre_quien_trae  VARCHAR(255),
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
