package com.ejemplo.pedidos.controller;


import com.ejemplo.pedidos.Producto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private List<Producto> productos;

    public ProductoController() {
        productos = new ArrayList<>();
        productos.add(new Producto(103L, "Placa Universal Audio", 900, 19));
        productos.add(new Producto(104L, "Consola DDJ400 Pioneer", 600, 1));
    }


    @GetMapping
    public List<Producto> listar(){
        return productos;
    }


}
