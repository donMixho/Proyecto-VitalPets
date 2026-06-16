CREATE TABLE IF NOT EXISTS mascotas (
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    nombre        VARCHAR(255) NOT NULL,
    especie       VARCHAR(50)  NOT NULL,
    raza          VARCHAR(255),
    edad_anios    INT          NOT NULL,
    sexo          VARCHAR(255),
    peso_kg       DOUBLE,
    color_pelaje  VARCHAR(255),
    notas_especie VARCHAR(500),
    cliente_id    BIGINT       NOT NULL,
    activo        BIT          NOT NULL DEFAULT 1,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
