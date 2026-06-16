CREATE TABLE IF NOT EXISTS examenes_laboratorio (
    id               BIGINT        NOT NULL AUTO_INCREMENT,
    mascota_id       BIGINT        NOT NULL,
    cliente_id       BIGINT        NOT NULL,
    personal_id      BIGINT        NOT NULL,
    cita_id          BIGINT,
    tipo_examen      VARCHAR(50)   NOT NULL,
    estado           VARCHAR(50)   DEFAULT 'SOLICITADO',
    fecha_solicitud  DATETIME(6),
    fecha_resultado  DATETIME(6),
    resultados       VARCHAR(2000),
    observaciones    VARCHAR(500),
    archivo_resultado VARCHAR(255),
    urgente          BIT           DEFAULT 0,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
