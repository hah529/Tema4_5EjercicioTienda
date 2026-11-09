package com.daw2.tienda.productos.services;

import com.daw2.tienda.categorias.models.Categoria;
import com.daw2.tienda.categorias.repositories.CategoriasRepository;
import com.daw2.tienda.productos.dto.ProductoCreateRequest;
import com.daw2.tienda.productos.dto.ProductoResponse;
import com.daw2.tienda.productos.dto.ProductoUpdateRequest;
import com.daw2.tienda.productos.dto.VentaRequest;
import com.daw2.tienda.productos.exceptions.ProductoBadRequestException;
import com.daw2.tienda.productos.exceptions.ProductoConflictException;
import com.daw2.tienda.productos.exceptions.ProductoNotFoundException;
import com.daw2.tienda.productos.mappers.ProductoMapper;
import com.daw2.tienda.productos.repositories.ProductosRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductosServiceImpl implements ProductosService {

    private static final Logger log = LoggerFactory.getLogger(ProductosServiceImpl.class);

    private final ProductosRepository productosRepository;
    private final CategoriasRepository categoriasRepository;
    private final ProductoMapper mapper;

    public ProductosServiceImpl(ProductosRepository productosRepository,
                                CategoriasRepository categoriasRepository,
                                ProductoMapper mapper) {
        this.productosRepository = productosRepository;
        this.categoriasRepository = categoriasRepository;
        this.mapper = mapper;
    }

    // ─────────────────────────── LECTURAS ───────────────────────────

    @Override
    @Cacheable
    public List<ProductoResponse> findAll(Optional<String> categoria,
                                          Optional<String> nombre,
                                          Optional<Boolean> conStock) {

        // Si se filtra por categoria, hay que traducir el NOMBRE a su id
        Long categoriaId = categoria
                .map(this::buscarCategoriaPorNombre)
                .map(Categoria::id)
                .orElse(null);

        var productos = productosRepository.buscar(
                categoriaId, nombre.orElse(null), conStock.orElse(null));

        // Una sola consulta para TODAS las categorias, y se mapea en memoria
        var nombresPorId = nombresDeCategorias();

        return productos.stream()
                .map(p -> mapper.toResponse(p, nombresPorId.get(p.categoriaId())))
                .toList();
    }

    @Override
    @Cacheable(key = "#id")
    public ProductoResponse findById(Long id) {
        var producto = productosRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException(id));

        return mapper.toResponse(producto, nombreDeCategoria(producto.categoriaId()));
    }

    // ─────────────────────────── ESCRITURAS ───────────────────────────

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public ProductoResponse save(ProductoCreateRequest request) {

        // R1 · el SKU no puede repetirse
        if (productosRepository.existsBySku(request.sku())) {
            throw new ProductoConflictException("Ya existe un producto con el SKU " + request.sku());
        }

        // R2 · la categoria tiene que existir
        var categoria = buscarCategoriaPorNombre(request.categoria());

        var guardado = productosRepository.save(mapper.toModel(request, categoria.id()));

        log.info("Producto creado: {} ({})", guardado.sku(), guardado.id());
        return mapper.toResponse(guardado, categoria.nombre());
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public ProductoResponse update(Long id, ProductoUpdateRequest request) {

        var original = productosRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException(id));

        // Si llega categoria, se valida; si no, se conserva la que tenia
        Long categoriaId = request.categoria() == null
                ? null
                : buscarCategoriaPorNombre(request.categoria()).id();

        var actualizado = productosRepository.update(
                mapper.parchear(original, request, categoriaId));

        return mapper.toResponse(actualizado, nombreDeCategoria(actualizado.categoriaId()));
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public void deleteById(Long id) {

        var producto = productosRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException(id));

        // R3 · no se borra un producto que todavia tiene stock
        if (producto.stock() > 0) {
            throw new ProductoConflictException(
                    "No se puede borrar %s: todavía tiene %d unidades en stock"
                            .formatted(producto.sku(), producto.stock()));
        }

        productosRepository.deleteById(id);
        log.info("Producto borrado: {}", id);
    }

    /**
     * R4 · Vender: descuenta stock del producto Y suma al contador de su categoria.
     * Las dos cosas, o ninguna.
     */
    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public ProductoResponse vender(Long id, VentaRequest request) {

        var producto = productosRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException(id));

        // 1 · descontar de forma atomica: 0 filas = no habia bastante
        if (productosRepository.descontarStock(id, request.cantidad()) == 0) {
            throw new ProductoConflictException(
                    "Stock insuficiente de %s: se piden %d y hay %d"
                            .formatted(producto.nombre(), request.cantidad(), producto.stock()));
        }

        // 2 · sumar al contador de la categoria
        categoriasRepository.sumarVendidas(producto.categoriaId(), request.cantidad());

        log.info("Vendidas {} unidades de {}", request.cantidad(), producto.sku());

        var actualizado = productosRepository.findById(id).orElseThrow();
        return mapper.toResponse(actualizado, nombreDeCategoria(actualizado.categoriaId()));
    }

    // ─────────────────────────── AUXILIARES ───────────────────────────

    /** Busca una categoria por nombre, o lanza 400 si no existe. */
    private Categoria buscarCategoriaPorNombre(String nombre) {
        return categoriasRepository.findByNombre(nombre)
                .orElseThrow(() -> new ProductoBadRequestException(
                        "La categoría '" + nombre + "' no existe"));
    }

    /** El nombre de una categoria por su id. */
    private String nombreDeCategoria(Long categoriaId) {
        return categoriasRepository.findById(categoriaId)
                .map(Categoria::nombre)
                .orElse("SIN CATEGORÍA");
    }

    /** Todas las categorias de una vez: id → nombre. */
    private Map<Long, String> nombresDeCategorias() {
        return categoriasRepository.findAll().stream()
                .collect(Collectors.toMap(Categoria::id, Categoria::nombre));
    }
}