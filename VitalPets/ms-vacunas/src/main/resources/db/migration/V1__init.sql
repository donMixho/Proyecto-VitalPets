CREATE TABLE IF NOT EXISTS vacunas (
    id                   BIGINT       NOT NULL AUTO_INCREMENT,
    mascota_id           BIGINT       NOT NULL,
    cliente_id           BIGINT       NOT NULL,
    personal_id          BIGINT       NOT NULL,
    nombre_vacuna        VARCHAR(255),
    laboratorio          VARCHAR(255),
    lote                 VARCHAR(255),
    fecha_aplicacion     DATE         NOT NULL,
    fecha_proxima_dosis  DATE,
    dosis                VARCHAR(255),
    observaciones        VARCHAR(500),
    vigente              BIT          DEFAULT 1,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
