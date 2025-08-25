-- =====================
-- TABLA: estados
-- =====================
CREATE TABLE estados (
    id_estado BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255)
);

-- =====================
-- TABLA: tipo_prestamo
-- =====================
CREATE TABLE tipo_prestamo (
    id_tipo_prestamo BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    monto_minimo NUMERIC(12,2) NOT NULL,
    monto_maximo NUMERIC(12,2) NOT NULL,
    tasa_interes NUMERIC(5,2) NOT NULL, -- ejemplo: 12.50%
    validacion_automatica BOOLEAN DEFAULT FALSE
);

-- =====================
-- TABLA: solicitud
-- =====================
CREATE TABLE solicitud (
    id_solicitud BIGSERIAL PRIMARY KEY,
    monto NUMERIC(12,2) NOT NULL,
    plazo INT NOT NULL, -- en meses
    email VARCHAR(150) NOT NULL,
    id_estado BIGINT NOT NULL,
    id_tipo_prestamo BIGINT NOT NULL,
    CONSTRAINT fk_solicitud_estado FOREIGN KEY (id_estado)
        REFERENCES estados(id_estado)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_solicitud_tipo_prestamo FOREIGN KEY (id_tipo_prestamo)
        REFERENCES tipo_prestamo(id_tipo_prestamo)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

INSERT INTO tipo_prestamo (nombre, monto_minimo, monto_maximo, tasa_interes, validacion_automatica)
VALUES
('Préstamo Normal', 500.00, 5000.00, 8.50, TRUE),
('Préstamo Alto', 5001.00, 50000.00, 12.75, FALSE);

INSERT INTO estados (nombre, descripcion)
VALUES
('Pendiente de revisión', 'La solicitud fue recibida y está en proceso de análisis inicial');
