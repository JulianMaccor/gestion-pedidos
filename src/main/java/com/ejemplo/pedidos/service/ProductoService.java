package com.ejemplo.pedidos.service;
import com.ejemplo.pedidos.dto.CreateProductDto;
import com.ejemplo.pedidos.exception.ProductoNoEncontradoException;
import com.ejemplo.pedidos.model.Producto;
import com.ejemplo.pedidos.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository repository;

    public ProductoService(ProductoRepository repository) {
        this.repository = repository;
    }

    public List<Producto> listarProductos() {
        return repository.findAll();
    }

    public Producto buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProductoNoEncontradoException(id));
    }


    public Producto crearProducto(CreateProductDto dto) {
        Producto producto = new Producto(null, dto.getNombre(), dto.getPrecio(), dto.getStock());
        return repository.save(producto);
    }

    public Producto actualizarProductoPorId(Long id, CreateProductDto dto) {
        Producto producto = buscarPorId(id);
        producto.setNombre(dto.getNombre());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        return repository.save(producto);
    }

    public void eliminarProducto(Long id) {
        Producto producto = buscarPorId(id);
        repository.delete(producto);
    }
}
