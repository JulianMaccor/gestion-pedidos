package com.ejemplo.pedidos.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class CreatePedidoDto {

    @NotEmpty(message = "Debe incluir al menos un item")
    @Valid
    private List<ItemPedidoDto> items;

    public CreatePedidoDto() {
    }

    public CreatePedidoDto(List<ItemPedidoDto> items) {
        this.items = items;
    }

    public List<ItemPedidoDto> getItems() {
        return items;
    }

    public void setItems(List<ItemPedidoDto> items) {
        this.items = items;
    }
}

