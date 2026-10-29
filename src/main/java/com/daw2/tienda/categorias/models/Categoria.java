package com.daw2.tienda.categorias.models;

public record Categoria(
        Long id,
        String nombre,
        Integer unidadesVendidas
) {}
