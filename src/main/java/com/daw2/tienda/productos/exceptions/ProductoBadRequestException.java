package com.daw2.tienda.productos.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProductoBadRequestException extends RuntimeException {
    public ProductoBadRequestException(String message) {
        super(message);
    }
}
