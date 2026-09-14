package com.ejemplo.pedidos.controller;

import com.ejemplo.pedidos.dto.CreatePedidoDto;
import com.ejemplo.pedidos.dto.PedidoResponseDto;
import com.ejemplo.pedidos.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<PedidoResponseDto> crearPedido(@Valid @RequestBody CreatePedidoDto dto) {
        PedidoResponseDto pedidoCreado = pedidoService.crearPedido(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoCreado);
    }
}
