CREATE TABLE rol (
    uniqueid BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255)
);

CREATE TABLE usuario (
    id_usuario BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    documento_identidad VARCHAR(50) UNIQUE NOT NULL,
    telefono VARCHAR(20),
    id_rol BIGINT,
    salario_base NUMERIC(10,2),
    CONSTRAINT fk_usuario_rol FOREIGN KEY (id_rol)
        REFERENCES rol(uniqueid)
        ON UPDATE CASCADE
        ON DELETE SET NULL
);
