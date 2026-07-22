package com.ejemplo.pedidos.dto;

public class CategoriaResponseDto {
    private Long id;
    private String nombre;

    public CategoriaResponseDto(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public Long getId() {
        return id;
    }
}
