package com.ejemplo.pedidos.service;

import com.ejemplo.pedidos.dto.CreatePedidoDto;
import com.ejemplo.pedidos.dto.DetallePedidoResponseDto;
import com.ejemplo.pedidos.dto.ItemPedidoDto;
import com.ejemplo.pedidos.dto.PedidoResponseDto;
import com.ejemplo.pedidos.exception.PedidoNoEncontradoException;
import com.ejemplo.pedidos.exception.ProductoNoEncontradoException;
import com.ejemplo.pedidos.exception.StockInsuficienteException;
import com.ejemplo.pedidos.model.DetallePedido;
import com.ejemplo.pedidos.model.Pedido;
import com.ejemplo.pedidos.model.Producto;
import com.ejemplo.pedidos.model.Usuario;
import com.ejemplo.pedidos.repository.PedidoRepository;
import com.ejemplo.pedidos.repository.ProductoRepository;
import com.ejemplo.pedidos.repository.UsuarioRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    private final ProductoRepository productoRepository;

    private final UsuarioRepository usuarioRepository;

    public PedidoService(PedidoRepository pedidoRepository, ProductoRepository productoRepository, UsuarioRepository usuarioRepository) {
        this.pedidoRepository = pedidoRepository;
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    private Usuario obtenerUsuarioAutenticado(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByUsername(username)
                .orElseThrow(()-> new RuntimeException("Usuario no encontrado: " + username));
    }

    private boolean esAdmin(Usuario usuario) {
        // Usuario.rol is stored unprefixed in the database (e.g. "ADMIN").
        // Spring Security's UsuarioDetailsService calls .roles(usuario.getRol()),
        // and it is Spring's roles() method that adds the "ROLE_" prefix to build
        // the ROLE_ADMIN authority used elsewhere by @PreAuthorize("hasRole('ADMIN')").
        // This comparison reads Usuario.rol directly, so it must use the unprefixed literal.
        return "ADMIN".equals(usuario.getRol());
    }

    @Transactional
    public PedidoResponseDto crearPedido(CreatePedidoDto dto){
        Usuario usuario = obtenerUsuarioAutenticado();

        Pedido pedido = new Pedido(LocalDateTime.now());
        pedido.setUsuario(usuario);

        double total = 0;

        for (ItemPedidoDto item : dto.getItems()){
            Producto producto = productoRepository.findById(item.getProductoId())
                    .orElseThrow(()-> new ProductoNoEncontradoException(item.getProductoId()));
            if (producto.getStock() < item.getCantidad()){
                throw new StockInsuficienteException(producto.getNombre());
            }
            producto.setStock(producto.getStock() - item.getCantidad());

            DetallePedido detalle = new DetallePedido(producto, item.getCantidad(), producto.getPrecio());
            detalle.setPedido(pedido);
            pedido.getDetalles().add(detalle);

            total += producto.getPrecio() * item.getCantidad();
        }
        pedido.setTotal(total);

        Pedido pedidoGuardado = pedidoRepository.save(pedido);
        return toResponse(pedidoGuardado);
    }

    @Transactional(readOnly = true)
    public List<PedidoResponseDto> listarPedidos() {
        Usuario usuario = obtenerUsuarioAutenticado();

        List<Pedido> pedidos = esAdmin(usuario)
                ? pedidoRepository.findAll()
                : pedidoRepository.findByUsuarioId(usuario.getId());

        return pedidos.stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PedidoResponseDto buscarPorId(Long id) {
        Usuario usuario = obtenerUsuarioAutenticado();

        Pedido pedido = esAdmin(usuario)
                ? pedidoRepository.findById(id).orElseThrow(() -> new PedidoNoEncontradoException(id))
                : pedidoRepository.findByIdAndUsuarioId(id, usuario.getId())
                        .orElseThrow(() -> new PedidoNoEncontradoException(id));

        return toResponse(pedido);
    }

    private PedidoResponseDto toResponse(Pedido pedido) {
        List<DetallePedidoResponseDto> detalles = pedido.getDetalles().stream()
                .map(d -> new DetallePedidoResponseDto(
                        d.getProducto().getId(),
                        d.getProducto().getNombre(),
                        d.getCantidad(),
                        d.getPrecioUnitario(),
                        d.getCantidad() * d.getPrecioUnitario()
                ))
                .toList();

        return new PedidoResponseDto(
                pedido.getId(),
                pedido.getFecha(),
                pedido.getTotal(),
                pedido.getUsuario().getUsername(),
                detalles
        );
    }
}
