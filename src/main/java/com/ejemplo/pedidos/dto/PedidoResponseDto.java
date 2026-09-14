package com.ejemplo.pedidos.dto;

import java.time.LocalDateTime;
import java.util.List;

public class PedidoResponseDto {

    private Long id;

    private LocalDateTime fecha;

    private double total;

    private String usuario;

    private List<DetallePedidoResponseDto> detalles;

    public PedidoResponseDto(Long id, LocalDateTime fecha, double total, String usuario, List<DetallePedidoResponseDto> detalles) {
        this.id = id;
        this.fecha = fecha;
        this.total = total;
        this.usuario = usuario;
        this.detalles = detalles;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public double getTotal() {
        return total;
    }

    public String getUsuario() {
        return usuario;
    }

    public List<DetallePedidoResponseDto> getDetalles() {
        return detalles;
    }
}
