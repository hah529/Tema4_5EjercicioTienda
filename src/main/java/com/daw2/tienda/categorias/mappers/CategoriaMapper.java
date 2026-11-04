package com.daw2.tienda.categorias.mappers;

import com.daw2.tienda.categorias.models.Categoria;
import com.daw2.tienda.productos.dto.CategoriaResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoriaMapper    {
    public CategoriaResponse toResponse(Categoria c) {
        return new CategoriaResponse(c.id(), c.nombre(), c.unidadesVendidas());
    }

    public List<CategoriaResponse> toResponse(List<Categoria> categorias) {
        return categorias.stream().map(this::toResponse).toList();
    }
}
