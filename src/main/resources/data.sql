MERGE INTO categorias (id, nombre, unidades_vendidas) KEY(id) VALUES

    (1, 'INFORMATICA', 0),

    (2, 'PAPELERIA',   0),

    (3, 'MOBILIARIO',  0);



MERGE INTO productos (id, sku, nombre, precio, stock, categoria_id) KEY(id) VALUES

    (1, 'SKU-001', 'Teclado mecánico', 79.90, 10, 1),

    (2, 'SKU-002', 'Ratón vertical',   45.50,  3, 1),

    (3, 'SKU-003', 'Monitor 27 pulgadas', 229.00, 1, 1),

    (4, 'SKU-004', 'Cuaderno A4',       4.95, 50, 2),

    (5, 'SKU-005', 'Silla de oficina', 149.00,  0, 3);



ALTER TABLE categorias ALTER COLUMN id RESTART WITH 4;

ALTER TABLE productos  ALTER COLUMN id RESTART WITH 6;