package com.ejemplo.pedidos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class CreateProductDto {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Positive(message = "El valor debe ser positivo")
    private double precio;

    @PositiveOrZero(message = "El stock no puede ser negativo")
    private int stock;

    public CreateProductDto(String nombre, double precio, int stock) {
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
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
}
