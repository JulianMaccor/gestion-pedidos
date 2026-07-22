package com.ejemplo.pedidos.controller;

import com.ejemplo.pedidos.model.Categoria;
import com.ejemplo.pedidos.service.CategoriaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public List<Categoria> listar() {
        return categoriaService.listar();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Categoria> crear(@RequestBody Categoria categoria){
        Categoria creada = categoriaService.crearCategoria(categoria.getNombre());
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }
}
