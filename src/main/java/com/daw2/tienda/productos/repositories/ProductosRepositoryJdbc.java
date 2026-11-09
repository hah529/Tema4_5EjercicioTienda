package com.daw2.tienda.productos.repositories;

import com.daw2.tienda.productos.Producto;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductosRepositoryJdbc implements ProductosRepository {

    private final JdbcClient jdbc;
    private final ProductoRowMapper rowMapper;

    public ProductosRepositoryJdbc(JdbcClient jdbc, ProductoRowMapper rowMapper) {
        this.jdbc = jdbc;
        this.rowMapper = rowMapper;
    }

    @Override
    public List<Producto> buscar(Long categoriaId, String nombre, Boolean conStock) {
        var sql = new StringBuilder("SELECT * FROM productos WHERE 1 = 1");
        var params = new HashMap<String, Object>();

        if (categoriaId != null) {
            sql.append(" AND categoria_id = :categoriaId");
            params.put("categoriaId", categoriaId);
        }
        if (nombre != null) {
            sql.append(" AND UPPER(nombre) LIKE UPPER(:nombre)");
            params.put("nombre", "%" + nombre + "%");
        }
        if (conStock != null) {
            sql.append(conStock ? " AND stock > 0" : " AND stock = 0");
        }
        sql.append(" ORDER BY id");

        return jdbc.sql(sql.toString())
                .params(params)
                .query(rowMapper)
                .list();
    }

    @Override
    public Optional<Producto> findById(Long id) {
        return jdbc.sql("SELECT * FROM productos WHERE id = :id")
                .param("id", id)
                .query(rowMapper)
                .optional();
    }

    @Override
    public boolean existsBySku(String sku) {
        return jdbc.sql("SELECT count(*) FROM productos WHERE UPPER(sku) = UPPER(:sku)")
                .param("sku", sku)
                .query(Long.class)
                .single() > 0;
    }

    @Override
    public long contarPorCategoria(Long categoriaId) {
        return jdbc.sql("SELECT count(*) FROM productos WHERE categoria_id = :id")
                .param("id", categoriaId)
                .query(Long.class)
                .single();
    }

    @Override
    public Producto save(Producto p) {
        var keys = new GeneratedKeyHolder();

        jdbc.sql("""
                INSERT INTO productos (sku, nombre, precio, stock, categoria_id,
                                       created_at, updated_at)
                VALUES (:sku, :nombre, :precio, :stock, :categoriaId,
                        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
                """)
                .param("sku", p.sku())
                .param("nombre", p.nombre())
                .param("precio", p.precio())
                .param("stock", p.stock())
                .param("categoriaId", p.categoriaId())
                .update(keys, "id");             // ← columna en minusculas (PostgreSQL)

        Number id = keys.getKey();
        if (id == null) {
            throw new IllegalStateException("La base de datos no ha devuelto la clave ID");
        }
        return findById(id.longValue()).orElseThrow();
    }

    @Override
    public Producto update(Producto p) {
        jdbc.sql("""
                UPDATE productos
                   SET nombre = :nombre, precio = :precio, stock = :stock,
                       categoria_id = :categoriaId, updated_at = CURRENT_TIMESTAMP
                 WHERE id = :id
                """)
                .param("id", p.id())
                .param("nombre", p.nombre())
                .param("precio", p.precio())
                .param("stock", p.stock())
                .param("categoriaId", p.categoriaId())
                .update();

        return findById(p.id()).orElseThrow();
    }

    @Override
    public boolean deleteById(Long id) {
        return jdbc.sql("DELETE FROM productos WHERE id = :id")
                .param("id", id)
                .update() > 0;
    }

    @Override
    public int descontarStock(Long id, int cantidad) {
        return jdbc.sql("""
                UPDATE productos
                   SET stock = stock - :cantidad, updated_at = CURRENT_TIMESTAMP
                 WHERE id = :id AND stock >= :cantidad
                """)
                .param("id", id)
                .param("cantidad", cantidad)
                .update();
    }
}

