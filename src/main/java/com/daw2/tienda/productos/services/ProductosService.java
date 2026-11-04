package com.daw2.tienda.productos.services;

import com.daw2.tienda.productos.dto.ProductoCreateRequest;
import com.daw2.tienda.productos.dto.ProductoResponse;
import com.daw2.tienda.productos.dto.ProductoUpdateRequest;
import com.daw2.tienda.productos.dto.VentaRequest;

import java.util.List;
import java.util.Optional;

public interface ProductosService {
    List<ProductoResponse> findAll(Optional<String> categoria,
                                   Optional<String> nombre,
                                   Optional<Boolean> conStock);
    ProductoResponse findById(Long id);
    ProductoResponse save(ProductoCreateRequest request);
    ProductoResponse update(Long id, ProductoUpdateRequest request);
    void deleteById(Long id);
    ProductoResponse vender(Long id, VentaRequest request);
}