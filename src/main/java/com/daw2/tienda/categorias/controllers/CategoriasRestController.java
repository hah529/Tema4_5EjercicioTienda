package com.daw2.tienda.categorias.controllers;

import com.daw2.tienda.categorias.services.CategoriasService;
import com.daw2.tienda.productos.dto.CategoriaResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("${api.path:/api}/${api.version:v1}/categorias")
public class CategoriasRestController {

    private final CategoriasService servicio;

    public CategoriasRestController(CategoriasService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> findAll() {
        return ResponseEntity.ok(servicio.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(servicio.findById(id));
    }
}