package com.ejemplo.pedidos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class CreateProductDto {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Positive(message = "El valor debe ser positivo")
    private double precio;

    @PositiveOrZero(message = "El stock no puede ser negativo")
    private int stock;

    @NotNull
    private Long categoriaId;


    public CreateProductDto() {
    }

    public CreateProductDto(String nombre, double precio, int stock, Long categoriaId) {
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.categoriaId = categoriaId;
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

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }
}
