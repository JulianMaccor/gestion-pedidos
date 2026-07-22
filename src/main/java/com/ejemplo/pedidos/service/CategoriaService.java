package com.ejemplo.pedidos.service;

import com.ejemplo.pedidos.model.Categoria;
import com.ejemplo.pedidos.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public Categoria crearCategoria(String nombre){
        Categoria categoria = new Categoria(nombre);
        return categoriaRepository.save(categoria);
    }

    public List<Categoria> listar(){
        return categoriaRepository.findAll();
    }
}
