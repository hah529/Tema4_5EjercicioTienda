package com.daw2.tienda.categorias.services;

import com.daw2.tienda.productos.dto.CategoriaResponse;

import java.util.List;

public interface CategoriasService {
    List<CategoriaResponse> findAll();

    CategoriaResponse findById(Long id);
}
