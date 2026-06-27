package com.ejemplo.pedidos.controller;


import com.ejemplo.pedidos.dto.CreateProductDto;
import com.ejemplo.pedidos.model.Producto;
import com.ejemplo.pedidos.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public List<Producto> listar(){
        return productoService.listarProductos();
    }

    @GetMapping("/{id}")
    public Producto buscarPorId(@PathVariable Long id) {
        return productoService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<Producto> crearProducto(@Valid @RequestBody CreateProductDto dto){
        Producto ProductoCreado= productoService.crearProducto(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductoCreado);
    }

    @PutMapping("/{id}")
    public Producto actualizarProducto(@PathVariable Long id, @Valid @RequestBody CreateProductDto dto){
        return productoService.actualizarProductoPorId(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id){
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }


}
