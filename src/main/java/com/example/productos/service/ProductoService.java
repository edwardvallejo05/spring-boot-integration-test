package com.example.productos.service;

import com.example.productos.domain.Producto;
import com.example.productos.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class ProductoService {

    private final ProductoRepository repository;

    public ProductoService(ProductoRepository repository) {
        this.repository = repository;
    }

    public List<Producto> listar() {
        return repository.findAll();
    }

    public Producto crear(String nombre, BigDecimal precio, Integer stock) {
        if (precio.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        Producto p = new Producto(nombre, precio, stock);
        return repository.save(p);
    }

    public Producto obtenerPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Producto no encontrado: " + id));
    }

    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Producto no encontrado: " + id);
        }
        repository.deleteById(id);
    }

    // Se agrega nuevo Método para actualizar un producto completo
    public Producto actualizar(Producto producto) {
        if (producto.getPrecio().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        if (producto.getStock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        if (!repository.existsById(producto.getId())) {
            throw new NotFoundException("Producto no encontrado: " + producto.getId());
        }
        return repository.save(producto);
    }

    // Se agrega nuevo Método para actualizar un producto parcialmente
    public Producto actualizarParcial(Long id, String nombre, BigDecimal precio, Integer stock) {
        Producto existente = obtenerPorId(id);

        if (nombre != null) {
            existente.setNombre(nombre);
        }
        if (precio != null) {
            if (precio.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("El precio no puede ser negativo");
            }
            existente.setPrecio(precio);
        }
        if (stock != null) {
            if (stock < 0) {
                throw new IllegalArgumentException("El stock no puede ser negativo");
            }
            existente.setStock(stock);
        }
        return repository.save(existente);
    }
}
