package com.ejemplo.pedidos.service;

import com.ejemplo.pedidos.dto.CreatePedidoDto;
import com.ejemplo.pedidos.dto.DetallePedidoResponseDto;
import com.ejemplo.pedidos.dto.ItemPedidoDto;
import com.ejemplo.pedidos.dto.PedidoResponseDto;
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
