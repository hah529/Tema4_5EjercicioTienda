package com.daw2.tienda.productos.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/** Entra en el PATCH: todo opcional, solo reglas de formato. */

public record ProductoUpdateRequest(

        @Size(max = 150) String nombre,

        @DecimalMin("0.0") BigDecimal precio,

        @PositiveOrZero Integer stock,

        String categoria

) {}