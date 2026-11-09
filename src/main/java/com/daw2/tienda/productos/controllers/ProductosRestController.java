package com.daw2.tienda.productos.controllers;

import com.daw2.tienda.productos.dto.ProductoCreateRequest;
import com.daw2.tienda.productos.dto.ProductoResponse;
import com.daw2.tienda.productos.dto.ProductoUpdateRequest;
import com.daw2.tienda.productos.dto.VentaRequest;
import com.daw2.tienda.productos.services.ProductosService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("${api.path:/api}/${api.version:v1}/productos")
public class ProductosRestController {

    private final ProductosService servicio;

    public ProductosRestController(ProductosService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public ResponseEntity<List<ProductoResponse>> findAll(
            @RequestParam Optional<String> categoria,
            @RequestParam Optional<String> nombre,
            @RequestParam Optional<Boolean> conStock) {

        return ResponseEntity.ok(servicio.findAll(categoria, nombre, conStock));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(servicio.findById(id));
    }

    @PostMapping
    public ResponseEntity<ProductoResponse> save(@Valid @RequestBody ProductoCreateRequest dto) {
        var creado = servicio.save(dto);
        return ResponseEntity
                .created(URI.create("/api/v1/productos/" + creado.id()))
                .body(creado);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductoResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ProductoUpdateRequest dto) {

        return ResponseEntity.ok(servicio.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        servicio.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/venta")
    public ResponseEntity<ProductoResponse> vender(
            @PathVariable Long id,
            @Valid @RequestBody VentaRequest dto) {

        return ResponseEntity.ok(servicio.vender(id, dto));
    }
}
