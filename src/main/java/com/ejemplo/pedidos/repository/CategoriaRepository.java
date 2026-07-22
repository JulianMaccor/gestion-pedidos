package com.ejemplo.pedidos.repository;

import com.ejemplo.pedidos.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
