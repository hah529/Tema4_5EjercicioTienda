package com.daw2.tienda.productos.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProductoNotFoundException extends RuntimeException {
    public ProductoNotFoundException(Long id) {
        super("No se ha encontrado el producto con id: " + id);
    }
}
