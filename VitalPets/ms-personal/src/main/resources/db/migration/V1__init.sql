CREATE TABLE IF NOT EXISTS personal (
    id                    BIGINT       NOT NULL AUTO_INCREMENT,
    nombre                VARCHAR(255),
    apellido              VARCHAR(255),
    rut                   VARCHAR(255),
    telefono              VARCHAR(255),
    email                 VARCHAR(255),
    profesion             VARCHAR(255),
    especialidad          VARCHAR(50),
    implementos_asignados VARCHAR(500),
    activo                BIT          DEFAULT 1,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
