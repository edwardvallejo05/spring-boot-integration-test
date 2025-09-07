package com.example.productos.controller;

import com.example.productos.domain.Producto;
import com.example.productos.repository.ProductoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;
import java.util.HashMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
// Se modifica la clase para que sea Publica y se pueda acceder a ella en los test
public class ProductoControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ProductoRepository repository;
    @Autowired
    private ObjectMapper objectMapper;

    private Long existingId;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        Producto p = new Producto("Laptop", new BigDecimal("2500.00"), 2);
        existingId = repository.save(p).getId();
    }

    @Test
    void listarProductosDevuelve200() throws Exception {
        mockMvc.perform(get("/productos"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void crearProductoDevuelve201() throws Exception {
        var body = objectMapper.writeValueAsString(new java.util.HashMap<String, Object>() {{
            put("nombre", "Auriculares");
            put("precio", "199.99");
            put("stock", 15);
        }});
        mockMvc.perform(post("/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nombre").value("Auriculares"));
    }

    // Se agregan nuevos para nuevos endpoints de actualizar productos 
    @Test
    void actualizarProducto() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("nombre", "Actualizado");
        body.put("precio", 200.00);
        body.put("stock", 8);

        mockMvc.perform(put("/productos/{id}", existingId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Actualizado"))
                .andExpect(jsonPath("$.precio").value(200.00))
                .andExpect(jsonPath("$.stock").value(8));
    }

    @Test
    void actualizarProductoNoExistente() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("nombre", "Actualizado");
        body.put("precio", 200.00);
        body.put("stock", 8);

        mockMvc.perform(put("/productos/99999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isNotFound());
    }

    @Test
    void actualizarParcialProducto() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("precio", 300.00);

        mockMvc.perform(patch("/productos/{id}", existingId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.precio").value(300.00));
    }

    @Test
    void actualizarParcialProductoNoExistente() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("precio", 300.00);

        mockMvc.perform(patch("/productos/99999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isNotFound());
    }

    @Test
    void obtenerProductoPorIdExistenteDevuelve200() throws Exception {
        mockMvc.perform(get("/productos/{id}", existingId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingId));
    }

    @Test
    void obtenerProductoInexistenteDevuelve404() throws Exception {
        mockMvc.perform(get("/productos/{id}", 9999))
                .andExpect(status().isNotFound());
    }

    // Se agregan nuevos test para validar las nuevas reglas de negocio y completar la cobertura al 100%
    @Test
    void crearProductoConPrecioNegativoDevuelve400() throws Exception {
        var body = objectMapper.writeValueAsString(new HashMap<String, Object>() {{
            put("nombre", "ProductoNegativo");
            put("precio", -10.00);
            put("stock", 5);
        }});
        mockMvc.perform(post("/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void crearProductoConStockNegativoDevuelve400() throws Exception {
        var body = objectMapper.writeValueAsString(new HashMap<String, Object>() {{
            put("nombre", "ProductoStockNegativo");
            put("precio", 10.00);
            put("stock", -2);
        }});
        mockMvc.perform(post("/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void actualizarProductoConPrecioNegativoDevuelve400() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("nombre", "Actualizado");
        body.put("precio", -100.00);
        body.put("stock", 8);

        mockMvc.perform(put("/productos/{id}", existingId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void actualizarProductoConStockNegativoDevuelve400() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("nombre", "Actualizado");
        body.put("precio", 100.00);
        body.put("stock", -8);

        mockMvc.perform(put("/productos/{id}", existingId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void actualizarParcialProductoConPrecioNegativoDevuelve400() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("precio", -300.00);

        mockMvc.perform(patch("/productos/{id}", existingId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void actualizarParcialProductoConStockNegativoDevuelve400() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("stock", -5);

        mockMvc.perform(patch("/productos/{id}", existingId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void eliminarProductoDevuelve204() throws Exception {
        mockMvc.perform(delete("/productos/{id}", existingId))
                .andExpect(status().isNoContent());
        assertThat(repository.findById(existingId)).isEmpty();
    }

    @Test
    void eliminarProductoNoExistente() throws Exception {
        mockMvc.perform(delete("/productos/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void actualizarParcialProductoConNombreExistenteDevuelve200() throws Exception {
        // El producto creado en setUp tiene nombre "Laptop"
        Map<String, Object> body = new HashMap<>();
        body.put("nombre", "Laptop");

        mockMvc.perform(patch("/productos/{id}", existingId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Laptop"));
    }
}
