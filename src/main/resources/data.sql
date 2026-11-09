INSERT INTO categorias (id, nombre, unidades_vendidas) VALUES
                                                           (1, 'INFORMATICA', 0),
                                                           (2, 'PAPELERIA',   0),
                                                           (3, 'MOBILIARIO',  0)
ON CONFLICT (id) DO NOTHING;

INSERT INTO productos (id, sku, nombre, precio, stock, categoria_id) VALUES
                                                                         (1, 'SKU-001', 'Teclado mecánico', 79.90, 10, 1),
                                                                         (2, 'SKU-002', 'Ratón vertical',   45.50,  3, 1),
                                                                         (3, 'SKU-003', 'Monitor 27 pulgadas', 229.00, 1, 1),
                                                                         (4, 'SKU-004', 'Cuaderno A4',       4.95, 50, 2),
                                                                         (5, 'SKU-005', 'Silla de oficina', 149.00,  0, 3)
ON CONFLICT (id) DO NOTHING;

-- Sincronizar el contador de IDs con el maximo real (seguro en cada reinicio: nunca retrocede)
SELECT setval(pg_get_serial_sequence('categorias', 'id'), (SELECT MAX(id) FROM categorias));
SELECT setval(pg_get_serial_sequence('productos', 'id'), (SELECT MAX(id) FROM productos));