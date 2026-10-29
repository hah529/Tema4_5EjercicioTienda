package com.daw2.tienda.productos.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record VentaRequest(

        @NotNull @Min(value = 1, message = "La cantidad mínima es 1")

        Integer cantidad

) {}