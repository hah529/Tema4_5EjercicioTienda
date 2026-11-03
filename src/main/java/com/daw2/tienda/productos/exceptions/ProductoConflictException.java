package com.daw2.tienda.productos.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ProductoConflictException extends RuntimeException {
    public ProductoConflictException(String message) {
        super(message);
    }
}
