package com.ejemplo.pedidos.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ItemPedidoDto {

    @NotNull(message = "El producto es obligatorio")
    private Long productoId;

    @Positive(message = "La cantidad debe ser positiva")
    private int cantidad;

    public ItemPedidoDto() {
    }

    public ItemPedidoDto(int cantidad, Long productoId) {
        this.cantidad = cantidad;
        this.productoId = productoId;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
