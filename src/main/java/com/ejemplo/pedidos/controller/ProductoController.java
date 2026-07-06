package com.ejemplo.pedidos.controller;


import com.ejemplo.pedidos.dto.CreateProductDto;
import com.ejemplo.pedidos.dto.ProductResponseDto;
import com.ejemplo.pedidos.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public List<ProductResponseDto> listar(){
        return productoService.listarProductos();
    }

    @GetMapping("/{id}")
    public ProductResponseDto buscarPorId(@PathVariable Long id) {
        return productoService.buscarPorId(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDto> crearProducto(@Valid @RequestBody CreateProductDto dto){
        ProductResponseDto productoCreado= productoService.crearProducto(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(productoCreado);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ProductResponseDto actualizarProducto(@PathVariable Long id, @Valid @RequestBody CreateProductDto dto){
        return productoService.actualizarProductoPorId(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id){
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }


}
