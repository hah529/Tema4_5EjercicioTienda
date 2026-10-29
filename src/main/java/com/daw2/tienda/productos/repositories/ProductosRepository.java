package com.daw2.tienda.productos.repositories;

import com.daw2.tienda.productos.Producto;

import java.util.List;
import java.util.Optional;

public interface ProductosRepository {

    /** Listado con tres filtros opcionales. Un null significa "no filtrar por esto". */
    List<Producto> buscar(Long categoriaId, String nombre, Boolean conStock);

    Optional<Producto> findById(Long id);

    boolean existsBySku(String sku);

    /** Cuántos productos tiene esa categoría. Para poder borrarla o no. */
    long contarPorCategoria(Long categoriaId);

    Producto save(Producto producto);

    Producto update(Producto producto);

    /** Devuelve true si ha borrado algo. */
    boolean deleteById(Long id);

    /** Descuenta stock SOLO si hay suficiente. Devuelve las filas afectadas. */
    int descontarStock(Long id, int cantidad);
}
