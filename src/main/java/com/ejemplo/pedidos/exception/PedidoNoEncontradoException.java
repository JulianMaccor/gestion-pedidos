package com.ejemplo.pedidos.exception;

public class PedidoNoEncontradoException extends RuntimeException {
    public PedidoNoEncontradoException(Long id) {
        super("No se encontró el pedido con id " + id);
    }
}
