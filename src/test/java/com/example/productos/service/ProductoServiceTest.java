package com.example.productos.service;

import com.example.productos.domain.Producto;
import com.example.productos.repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class ProductoServiceTest {

    @Autowired
    private ProductoService service;
    @Autowired
    private ProductoRepository repository;

    @Test
    void listarDevuelveProductos() {
        service.crear("Laptop", new BigDecimal("1200.00"), 2);
        service.crear("Mouse", new BigDecimal("25.00"), 10);
        assertThat(service.listar()).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    void crearYObtenerProducto() {
        Producto creado = service.crear("Monitor", new BigDecimal("599.90"), 3);
        Producto obtenido = service.obtenerPorId(creado.getId());
        assertThat(obtenido.getNombre()).isEqualTo("Monitor");
    }

    @Test
    void crearProductoConPrecioNegativoLanzaException() {
        assertThatThrownBy(() -> service.crear("ProductoNegativo", new BigDecimal("-1.00"), 5))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("precio no puede ser negativo");
    }

    @Test
    void crearProductoConStockNegativoLanzaException() {
        assertThatThrownBy(() -> service.crear("ProductoStockNegativo", new BigDecimal("10.00"), -2))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("stock no puede ser negativo");
    }

    @Test
    void obtenerPorIdNoExistenteLanzaException() {
        assertThatThrownBy(() -> service.obtenerPorId(999L))
            .isInstanceOf(NotFoundException.class)
            .hasMessageContaining("Producto no encontrado");
    }

    @Test
    void eliminarProductoNoExistenteLanzaExcepcion() {
        assertThatThrownBy(() -> service.eliminar(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Producto no encontrado");
    }

    @Test
    void actualizarProductoExistente() {
        Producto creado = service.crear("Tablet", new BigDecimal("299.99"), 7);
        creado.setPrecio(new BigDecimal("249.99"));
        creado.setStock(5);
        Producto actualizado = service.actualizar(creado);
        assertThat(actualizado.getPrecio()).isEqualByComparingTo("249.99");
        assertThat(actualizado.getStock()).isEqualTo(5);
    }

    @Test
    void actualizarProductoNoExistenteLanzaException() {
        Producto p = new Producto("NoExiste", new BigDecimal("10.00"), 1);
        p.setId(999L);
        assertThatThrownBy(() -> service.actualizar(p))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Producto no encontrado");
    }

    @Test
    void actualizarProductoConPrecioNegativoLanzaException() {
        Producto creado = service.crear("Smartwatch", new BigDecimal("199.99"), 3);
        creado.setPrecio(new BigDecimal("-5.00"));
        assertThatThrownBy(() -> service.actualizar(creado))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("precio no puede ser negativo");
    }

    @Test
    void actualizarProductoConStockNegativoLanzaException() {
        Producto creado = service.crear("Auriculares", new BigDecimal("59.99"), 8);
        creado.setStock(-1);
        assertThatThrownBy(() -> service.actualizar(creado))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("stock no puede ser negativo");
    }

    @Test
    void actualizarParcialProducto() {
        Producto creado = service.crear("Cámara", new BigDecimal("499.99"), 2);
        Producto actualizado = service.actualizarParcial(creado.getId(), null, new BigDecimal("450.00"), null);
        assertThat(actualizado.getPrecio()).isEqualByComparingTo("450.00");
        Producto actualizado2 = service.actualizarParcial(creado.getId(), "Cámaras", null, null);
        assertThat(actualizado.getNombre()).isEqualTo("Cámaras");
        Producto actualizado3 = service.actualizarParcial(creado.getId(), null, null, 5);
        assertThat(actualizado.getStock()).isEqualTo(5);
    }

    @Test
    void actualizarParcialProductoNoExistenteLanzaException() {
        assertThatThrownBy(() -> service.actualizarParcial(999L, "Nuevo", new BigDecimal("10.00"), 1))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Producto no encontrado");
    }

    @Test
    void actualizarParcialProductoConPrecioNegativoLanzaException() {
        Producto creado = service.crear("Impresora", new BigDecimal("150.00"), 3);
        assertThatThrownBy(() -> service.actualizarParcial(creado.getId(), null, new BigDecimal("-10.00"), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("precio no puede ser negativo");
    }

    @Test
    void actualizarParcialProductoConStockNegativoLanzaException() {
        Producto creado = service.crear("Router", new BigDecimal("80.00"), 5);
        assertThatThrownBy(() -> service.actualizarParcial(creado.getId(), null, null, -2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("stock no puede ser negativo");
    }
    @Test
    void eliminarProductoExistente() {
        Producto creado = service.crear("Teclado", new BigDecimal("99.99"), 4);
        service.eliminar(creado.getId());
        assertThatThrownBy(() -> service.obtenerPorId(creado.getId()))
                .isInstanceOf(NotFoundException.class);
    }
}
