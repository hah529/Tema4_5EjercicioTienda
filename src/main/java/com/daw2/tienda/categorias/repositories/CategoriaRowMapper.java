package com.daw2.tienda.categorias.repositories;

import com.daw2.tienda.categorias.models.Categoria;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CategoriaRowMapper implements RowMapper<Categoria> {

    @Override
    public Categoria mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Categoria(
                rs.getLong("id"),
                rs.getString("nombre"),
                rs.getInt("unidades_vendidas"));
    }
}
