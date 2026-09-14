package com.ejemplo.pedidos;

import com.ejemplo.pedidos.dto.CreatePedidoDto;
import com.ejemplo.pedidos.dto.ItemPedidoDto;
import com.ejemplo.pedidos.model.Producto;
import com.ejemplo.pedidos.model.Usuario;
import com.ejemplo.pedidos.repository.PedidoRepository;
import com.ejemplo.pedidos.repository.ProductoRepository;
import com.ejemplo.pedidos.repository.UsuarioRepository;
import com.ejemplo.pedidos.support.IntegrationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PedidoCreationIntegrationTest extends IntegrationTestBase {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    private Producto productoSeed;

    @BeforeEach
    void setUp() {
        pedidoRepository.deleteAll();
        productoRepository.deleteAll();
        usuarioRepository.findByUsername("cliente").ifPresentOrElse(
                u -> { },
                () -> usuarioRepository.save(new Usuario("cliente", "clave-encriptada", "USER"))
        );

        Producto producto = new Producto("Teclado mecanico", 100.0, 10);
        productoSeed = productoRepository.save(producto);
    }

    @Test
    @WithMockUser(username = "cliente")
    void crearPedido_decrementaStock() throws Exception {
        CreatePedidoDto dto = new CreatePedidoDto(
                List.of(new ItemPedidoDto(3, productoSeed.getId()))
        );

        String responseBody = mockMvc.perform(post("/pedidos")
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.fecha").exists())
                .andExpect(jsonPath("$.total").value(300.0))
                .andExpect(jsonPath("$.usuario").value("cliente"))
                .andExpect(jsonPath("$.detalles[0].productoId").value(productoSeed.getId()))
                .andExpect(jsonPath("$.detalles[0].nombre").value("Teclado mecanico"))
                .andExpect(jsonPath("$.detalles[0].cantidad").value(3))
                .andExpect(jsonPath("$.detalles[0].precioUnitario").value(100.0))
                .andExpect(jsonPath("$.detalles[0].subtotal").value(300.0))
                .andReturn().getResponse().getContentAsString();

        assertFalse(responseBody.toLowerCase().contains("password"), "Response body must never leak the password field");

        // Simulates an external observer reading state after the request finished.
        Producto productoActualizado = productoRepository.findById(productoSeed.getId()).orElseThrow();
        assertEquals(7, productoActualizado.getStock());
        assertEquals(1, pedidoRepository.count());
    }

    @Test
    @WithMockUser(username = "cliente")
    void crearPedido_stockInsuficiente_noPersisteNada() throws Exception {
        Producto productoBajoStock = productoRepository.save(new Producto("Mouse", 50.0, 2));

        CreatePedidoDto dto = new CreatePedidoDto(
                List.of(new ItemPedidoDto(5, productoBajoStock.getId()))
        );

        mockMvc.perform(post("/pedidos")
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict());

        assertEquals(0, pedidoRepository.count());
        Producto productoSinCambios = productoRepository.findById(productoBajoStock.getId()).orElseThrow();
        assertEquals(2, productoSinCambios.getStock());
    }

    @Test
    @WithMockUser(username = "cliente")
    void crearPedido_multiItem_fallaSegundoItem_revierteTodo() throws Exception {
        Producto productoA = productoRepository.save(new Producto("Monitor", 200.0, 5));
        Producto productoB = productoRepository.save(new Producto("Webcam", 80.0, 1));

        CreatePedidoDto dto = new CreatePedidoDto(
                List.of(
                        new ItemPedidoDto(3, productoA.getId()),
                        new ItemPedidoDto(2, productoB.getId())
                )
        );

        mockMvc.perform(post("/pedidos")
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict());

        assertEquals(0, pedidoRepository.count());
        Producto productoASinCambios = productoRepository.findById(productoA.getId()).orElseThrow();
        assertEquals(5, productoASinCambios.getStock());
    }

    @Test
    @WithMockUser(username = "cliente")
    void crearPedido_itemsVacios_retorna400() throws Exception {
        CreatePedidoDto dto = new CreatePedidoDto(List.of());

        mockMvc.perform(post("/pedidos")
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.items").exists());
    }

    @Test
    @WithMockUser(username = "cliente")
    void crearPedido_cantidadNoPositiva_retorna400() throws Exception {
        CreatePedidoDto dto = new CreatePedidoDto(
                List.of(new ItemPedidoDto(0, productoSeed.getId()))
        );

        mockMvc.perform(post("/pedidos")
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$['items[0].cantidad']").exists());
    }

    @Test
    @WithMockUser(username = "cliente")
    void crearPedido_productoIdNulo_retorna400() throws Exception {
        CreatePedidoDto dto = new CreatePedidoDto(
                List.of(new ItemPedidoDto(1, null))
        );

        mockMvc.perform(post("/pedidos")
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$['items[0].productoId']").exists());
    }
}
