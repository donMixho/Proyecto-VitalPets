CREATE TABLE IF NOT EXISTS historial_medico (
    id                      BIGINT        NOT NULL AUTO_INCREMENT,
    mascota_id              BIGINT        NOT NULL,
    cliente_id              BIGINT        NOT NULL,
    personal_id             BIGINT        NOT NULL,
    cita_id                 BIGINT,
    tipo_evento             VARCHAR(50)   NOT NULL,
    fecha_evento            DATETIME(6),
    descripcion             VARCHAR(1000) NOT NULL,
    diagnostico             VARCHAR(500),
    tratamiento             VARCHAR(500),
    medicamentos_recetados  VARCHAR(500),
    proxima_visita          VARCHAR(255),
    nombre_quien_trajo      VARCHAR(255),
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
