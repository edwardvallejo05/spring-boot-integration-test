package com.example.productos.repository;

import com.example.productos.domain.Producto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductoRepositoryTest {

    @Autowired
    private ProductoRepository repository;

    // Se agregan tests para completar la cobertura al 100% del repositorio ya que no existian
    @Test
    void buscarNombre() {
        Optional<Producto> found = repository.findByNombre("Proyector");
        assertThat(found).isPresent();
        assertThat(found.get().getNombre()).isEqualTo("Proyector");
    }

    @Test
    void buscarNombreNoExistente() {
        Optional<Producto> found = repository.findByNombre("NoExiste");
        assertThat(found).isEmpty();
    }

    @Test
    void guardarYBuscarPorId() {
        Producto p = new Producto("Teclado", new BigDecimal("99.99"), 10);
        Producto saved = repository.save(p);
        Optional<Producto> found = repository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getNombre()).isEqualTo("Teclado");
    }

    @Test
    void buscarIdNoExistente() {
        Optional<Producto> found = repository.findById(Long.MAX_VALUE);
        assertThat(found).isEmpty();
    }

    @Test
    void eliminarProducto() {
        Producto p = new Producto("Mouse", new BigDecimal("49.99"), 5);
        Producto saved = repository.save(p);
        repository.deleteById(saved.getId());
        assertThat(repository.findById(saved.getId())).isEmpty();
    }

    @Test
    void eliminarNoExistente() {
        repository.deleteById(Long.MAX_VALUE);
        assertThat(repository.findById(Long.MAX_VALUE)).isEmpty();
    }
}
