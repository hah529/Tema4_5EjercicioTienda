CREATE TABLE IF NOT EXISTS categorias (

                                          id                BIGINT AUTO_INCREMENT PRIMARY KEY,

                                          nombre            VARCHAR(40) NOT NULL,

    unidades_vendidas INT         NOT NULL DEFAULT 0,

    CONSTRAINT uk_categoria_nombre   UNIQUE (nombre),

    CONSTRAINT ck_categoria_vendidas CHECK  (unidades_vendidas >= 0)

    );



CREATE TABLE IF NOT EXISTS productos (

                                         id           BIGINT AUTO_INCREMENT PRIMARY KEY,

                                         sku          VARCHAR(20)   NOT NULL,

    nombre       VARCHAR(150)  NOT NULL,

    precio       DECIMAL(10,2) NOT NULL,

    stock        INT           NOT NULL DEFAULT 0,

    categoria_id BIGINT        NOT NULL,

    created_at   TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at   TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_producto_sku    UNIQUE (sku),

    CONSTRAINT ck_producto_precio CHECK  (precio >= 0),

    CONSTRAINT ck_producto_stock  CHECK  (stock  >= 0),

    CONSTRAINT fk_producto_categoria FOREIGN KEY (categoria_id) REFERENCES categorias (id)

    );