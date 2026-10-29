package com.daw2.tienda.productos.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/** Entra en el POST: la categoría se indica por NOMBRE. */

public record ProductoCreateRequest(



        @NotBlank

        @Pattern(regexp = "SKU-\\d{3}", message = "El SKU debe tener el formato SKU-nnn")

        String sku,



        @NotBlank @Size(max = 150)

        String nombre,



        @NotNull @DecimalMin(value = "0.0", message = "El precio no puede ser negativo")

        BigDecimal precio,



        @NotNull @PositiveOrZero

        Integer stock,



        @NotBlank(message = "La categoría es obligatoria")

        String categoria

) {}
