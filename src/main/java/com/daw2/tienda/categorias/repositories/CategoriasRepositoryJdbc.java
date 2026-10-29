package com.daw2.tienda.categorias.repositories;

import com.daw2.tienda.categorias.models.Categoria;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CategoriasRepositoryJdbc implements CategoriasRepository {

    private final JdbcClient jdbc;
    private final CategoriaRowMapper rowMapper;

    public CategoriasRepositoryJdbc(JdbcClient jdbc, CategoriaRowMapper rowMapper) {
        this.jdbc = jdbc;
        this.rowMapper = rowMapper;
    }

    @Override
    public List<Categoria> findAll() {
        return jdbc.sql("SELECT * FROM categorias ORDER BY id")
                .query(rowMapper)
                .list();
    }

    @Override
    public Optional<Categoria> findById(Long id) {
        return jdbc.sql("SELECT * FROM categorias WHERE id = :id")
                .param("id", id)
                .query(rowMapper)
                .optional();
    }

    @Override
    public Optional<Categoria> findByNombre(String nombre) {
        return jdbc.sql("SELECT * FROM categorias WHERE UPPER(nombre) = UPPER(:nombre)")
                .param("nombre", nombre)
                .query(rowMapper)
                .optional();
    }

    @Override
    public int sumarVendidas(Long id, int cantidad) {
        return jdbc.sql("""
                UPDATE categorias
                   SET unidades_vendidas = unidades_vendidas + :cantidad
                 WHERE id = :id
                """)
                .param("id", id)
                .param("cantidad", cantidad)
                .update();
    }
}