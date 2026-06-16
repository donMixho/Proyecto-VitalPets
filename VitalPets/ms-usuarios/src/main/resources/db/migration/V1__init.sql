CREATE TABLE IF NOT EXISTS usuarios (
    id              BIGINT       NOT NULL AUTO_INCREMENT,
    username        VARCHAR(255) NOT NULL UNIQUE,
    password        VARCHAR(255) NOT NULL,
    nombre_completo VARCHAR(255),
    rol             VARCHAR(50)  NOT NULL,
    activo          BIT          DEFAULT 1,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
