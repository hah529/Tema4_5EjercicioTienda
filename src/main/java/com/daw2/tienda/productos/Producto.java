package com.daw2.tienda.productos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Producto(
        Long id,
        String sku,
        String nombre,
        BigDecimal precio,
        Integer stock,
        Long categoriaId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
