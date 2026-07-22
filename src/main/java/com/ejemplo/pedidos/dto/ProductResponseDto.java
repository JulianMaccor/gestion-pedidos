package com.ejemplo.pedidos.dto;

public class ProductResponseDto {

    private Long id;

    private String nombre;

    private double precio;

    private int stock;

    private CategoriaResponseDto categoria;

    public ProductResponseDto(Long id, String nombre, double precio, int stock, CategoriaResponseDto categoria) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public int getStock() {
        return stock;
    }

    public CategoriaResponseDto getCategoria() {
        return categoria;
    }
}
