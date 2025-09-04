package com.example.productos.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ProductoTest {

    @Test
    void constructorYGetters() {
        Producto p = new Producto("Monitor", new BigDecimal("199.99"), 10);
        assertThat(p.getNombre()).isEqualTo("Monitor");
        assertThat(p.getPrecio()).isEqualByComparingTo("199.99");
        assertThat(p.getStock()).isEqualTo(10);
    }

    @Test
    void settersFuncionan() {
        Producto p = new Producto();
        p.setId(1L);
        p.setNombre("Teclado");
        p.setPrecio(new BigDecimal("49.99"));
        p.setStock(5);

        assertThat(p.getId()).isEqualTo(1L);
        assertThat(p.getNombre()).isEqualTo("Teclado");
        assertThat(p.getPrecio()).isEqualByComparingTo("49.99");
        assertThat(p.getStock()).isEqualTo(5);
    }

    @Test
    void equalsYHashCodePorId() {
        Producto p1 = new Producto();
        Producto p2 = new Producto();
        p1.setId(1L);
        p2.setId(1L);

        assertThat(p1).isEqualTo(p2);
        assertThat(p1.hashCode()).isEqualTo(p2.hashCode());
    }

    @Test
    void equalsConIdDistinto() {
        Producto p1 = new Producto();
        Producto p2 = new Producto();
        p1.setId(1L);
        p2.setId(2L);

        assertThat(p1).isNotEqualTo(p2);
    }

    @Test
    void equalsDevuelveTrueSiEsElMismoObjeto() {
        Producto p = new Producto("Monitor", new BigDecimal("199.99"), 10);
        p.setId(1L);
        assertThat(p.equals(p)).isTrue();
    }

    @Test
    void equalsDevuelveFalseSiNoEsProducto() {
        Producto p = new Producto("Monitor", new BigDecimal("199.99"), 10);
        p.setId(1L);
        Object otro = new Object();
        assertThat(p.equals(otro)).isFalse();
    }
}