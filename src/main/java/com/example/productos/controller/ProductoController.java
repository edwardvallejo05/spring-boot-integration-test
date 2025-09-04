package com.example.productos.controller;

import com.example.productos.domain.Producto;
import com.example.productos.service.NotFoundException;
import com.example.productos.service.ProductoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @GetMapping
    public List<Producto> listar() {
        return service.listar();
    }

    @PostMapping
    public ResponseEntity<Producto> crear(@RequestBody Map<String, Object> body) {
        String nombre = (String) body.get("nombre");
        BigDecimal precio = new BigDecimal(body.get("precio").toString());
        Integer stock = (Integer) body.get("stock");
        Producto creado = service.crear(nombre, precio, stock);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtener(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.obtenerPorId(id));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            service.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        try {
            String nombre = (String) body.get("nombre");
            BigDecimal precio = new BigDecimal(body.get("precio").toString());
            Integer stock = (Integer) body.get("stock");
            Producto existente = service.obtenerPorId(id);
            existente.setNombre(nombre);
            existente.setPrecio(precio);
            existente.setStock(stock);
            Producto actualizado = service.actualizar(existente);
            return ResponseEntity.ok(actualizado);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Producto> actualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        try {
            Producto existente = service.obtenerPorId(id);
            if (body.containsKey("nombre")) {
                existente.setNombre((String) body.get("nombre"));
            }
            if (body.containsKey("precio")) {
                existente.setPrecio(new BigDecimal(body.get("precio").toString()));
            }
            if (body.containsKey("stock")) {
                existente.setStock((Integer) body.get("stock"));
            }
            Producto actualizado = service.actualizar(existente);
            return ResponseEntity.ok(actualizado);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    
}
