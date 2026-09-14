package com.ejemplo.pedidos.dto;

public class DetallePedidoResponseDto {

    private Long productoId;

    private String nombre;

    private int cantidad;

    private double precioUnitario;

    private double subtotal;

    public DetallePedidoResponseDto(Long productoId, String nombre, int cantidad, double precioUnitario, double subtotal) {
        this.productoId = productoId;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
    }

    public Long getProductoId() {
        return productoId;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public double getSubtotal() {
        return subtotal;
    }
}
