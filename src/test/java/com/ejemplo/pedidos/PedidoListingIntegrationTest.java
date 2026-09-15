package com.ejemplo.pedidos;

import com.ejemplo.pedidos.model.DetallePedido;
import com.ejemplo.pedidos.model.Pedido;
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

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PedidoListingIntegrationTest extends IntegrationTestBase {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    private Usuario cliente;
    private Usuario otro;
    private Usuario admin;
    private Pedido pedidoDeCliente;
    private Pedido pedidoDeOtro;

    @BeforeEach
    void setUp() {
        pedidoRepository.deleteAll();
        productoRepository.deleteAll();
        usuarioRepository.deleteAll();

        cliente = usuarioRepository.save(new Usuario("cliente", "clave-encriptada", "USER"));
        otro = usuarioRepository.save(new Usuario("otro", "clave-encriptada", "USER"));
        // NOTE: rol is seeded directly into the DB as the unprefixed literal "ADMIN".
        // Spring Security's UsuarioDetailsService adds the "ROLE_" prefix at authentication
        // time via .roles(...); PedidoService reads Usuario.rol directly, so this seed
        // is what actually exercises the DB-role comparison, independent of any mock authority.
        admin = usuarioRepository.save(new Usuario("admin", "clave-encriptada", "ADMIN"));

        Producto producto = productoRepository.save(new Producto("Teclado mecanico", 100.0, 10));

        pedidoDeCliente = crearPedidoPersistido(cliente, producto);
        pedidoDeOtro = crearPedidoPersistido(otro, producto);
    }

    private Pedido crearPedidoPersistido(Usuario usuario, Producto producto) {
        Pedido pedido = new Pedido(LocalDateTime.now());
        pedido.setUsuario(usuario);

        DetallePedido detalle = new DetallePedido(producto, 1, producto.getPrecio());
        detalle.setPedido(pedido);
        pedido.getDetalles().add(detalle);
        pedido.setTotal(producto.getPrecio());

        return pedidoRepository.save(pedido);
    }

    @Test
    @WithMockUser(username = "cliente")
    void listarPedidos_usuarioRegular_veSoloSusPropiosPedidos() throws Exception {
        String responseBody = mockMvc.perform(get("/pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(pedidoDeCliente.getId()))
                .andExpect(jsonPath("$[0].usuario").value("cliente"))
                .andReturn().getResponse().getContentAsString();

        assertFalse(responseBody.toLowerCase().contains("password"), "Response body must never leak the password field");
    }

    @Test
    @WithMockUser(username = "admin")
    void listarPedidos_admin_veTodosLosPedidos() throws Exception {
        // Deliberately NOT using @WithMockUser(roles = "ADMIN") here: that would only set a
        // mock authority and never touch the DB, so this test would falsely pass even with
        // a "ROLE_ADMIN" bug in PedidoService.esAdmin(...). This relies on the "admin" user
        // seeded above with the DB column rol = "ADMIN".
        String responseBody = mockMvc.perform(get("/pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andReturn().getResponse().getContentAsString();

        assertFalse(responseBody.toLowerCase().contains("password"), "Response body must never leak the password field");
    }

    @Test
    @WithMockUser(username = "cliente")
    void buscarPorId_usuarioRegular_suPropioPedido_retorna200() throws Exception {
        String responseBody = mockMvc.perform(get("/pedidos/" + pedidoDeCliente.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pedidoDeCliente.getId()))
                .andReturn().getResponse().getContentAsString();

        assertFalse(responseBody.toLowerCase().contains("password"), "Response body must never leak the password field");
    }

    @Test
    @WithMockUser(username = "cliente")
    void buscarPorId_usuarioRegular_pedidoDeOtroUsuario_retorna404() throws Exception {
        String responseBody = mockMvc.perform(get("/pedidos/" + pedidoDeOtro.getId()))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        assertEquals("No se encontró el pedido con id " + pedidoDeOtro.getId(), responseBody);
        assertFalse(responseBody.toLowerCase().contains("password"), "Response body must never leak the password field");
    }

    @Test
    @WithMockUser(username = "cliente")
    void buscarPorId_usuarioRegular_pedidoInexistente_retorna404_mismaFormaQueAjeno() throws Exception {
        long idInexistente = 999999L;

        String responseBody = mockMvc.perform(get("/pedidos/" + idInexistente))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();

        // Same exception message format as the "not yours" case in the previous test:
        // both hit PedidoNoEncontradoException from an empty Optional, so a caller
        // cannot distinguish "not yours" from "doesn't exist" via response shape (no
        // existence oracle over sequential IDENTITY ids).
        assertEquals("No se encontró el pedido con id " + idInexistente, responseBody);
        assertFalse(responseBody.toLowerCase().contains("password"), "Response body must never leak the password field");
    }

    @Test
    @WithMockUser(username = "admin")
    void buscarPorId_admin_cualquierPedido_retorna200() throws Exception {
        String responseClienteBody = mockMvc.perform(get("/pedidos/" + pedidoDeCliente.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pedidoDeCliente.getId()))
                .andReturn().getResponse().getContentAsString();

        String responseOtroBody = mockMvc.perform(get("/pedidos/" + pedidoDeOtro.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pedidoDeOtro.getId()))
                .andReturn().getResponse().getContentAsString();

        assertFalse(responseClienteBody.toLowerCase().contains("password"), "Response body must never leak the password field");
        assertFalse(responseOtroBody.toLowerCase().contains("password"), "Response body must never leak the password field");
    }
}
