package com.daw2.tienda.productos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Sale hacia el cliente: con el NOMBRE de la categoría, no su id. */
public record ProductoResponse(
        Long id,
        String sku,
        String nombre,
        BigDecimal precio,
        Integer stock,
        String categoria,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}