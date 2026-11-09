package com.daw2.tienda.categorias.services;

import com.daw2.tienda.categorias.exceptions.CategoriaNotFoundException;
import com.daw2.tienda.categorias.mappers.CategoriaMapper;
import com.daw2.tienda.categorias.repositories.CategoriasRepository;
import com.daw2.tienda.productos.dto.CategoriaResponse;
import com.daw2.tienda.productos.repositories.ProductosRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriasServiceImpl implements CategoriasService {

    private final CategoriasRepository categoriasRepository;
    private final ProductosRepository productosRepository;
    private final CategoriaMapper mapper;

    public CategoriasServiceImpl(CategoriasRepository categoriasRepository,
                                 ProductosRepository productosRepository,
                                 CategoriaMapper mapper) {
        this.categoriasRepository = categoriasRepository;
        this.productosRepository = productosRepository;
        this.mapper = mapper;
    }

    @Override
    public List<CategoriaResponse> findAll() {
        return mapper.toResponse(categoriasRepository.findAll());
    }

    @Override
    public CategoriaResponse findById(Long id) {
        return categoriasRepository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new CategoriaNotFoundException(id));
    }
}