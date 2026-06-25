package com.ejemplo.pedidos.controller;


import com.ejemplo.pedidos.dto.CreateProductDto;
import com.ejemplo.pedidos.model.Producto;
import com.ejemplo.pedidos.service.ProductoService;
import jakarta.validation.Valid;
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

    @PostMapping
    public Producto crearProducto(@Valid @RequestBody CreateProductDto dto){
        return productoService.crearProducto(dto);
    }


}
