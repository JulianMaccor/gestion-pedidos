package com.ejemplo.pedidos.service;
import com.ejemplo.pedidos.dto.CategoriaResponseDto;
import com.ejemplo.pedidos.dto.CreateProductDto;
import com.ejemplo.pedidos.dto.ProductResponseDto;
import com.ejemplo.pedidos.exception.CategoriaNoEncontradaException;
import com.ejemplo.pedidos.exception.ProductoNoEncontradoException;
import com.ejemplo.pedidos.model.Categoria;
import com.ejemplo.pedidos.model.Producto;
import com.ejemplo.pedidos.repository.CategoriaRepository;
import com.ejemplo.pedidos.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository repository;
    private final CategoriaRepository categoriaRepository;

    public ProductoService(ProductoRepository repository, CategoriaRepository categoriaRepository) {
        this.repository = repository;
        this.categoriaRepository = categoriaRepository;
    }

    private Producto obtenerEntidad(Long id){
        return repository.findById(id)
                .orElseThrow(()-> new ProductoNoEncontradoException(id));
    }

    private Categoria obtenerCategoria(Long categoriaId) {
        return categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new CategoriaNoEncontradaException(categoriaId));
    }

    public List<ProductResponseDto> listarProductos(){
        return repository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public ProductResponseDto buscarPorId(Long id) {
        Producto producto = obtenerEntidad(id);
        return toResponse(producto);
    }


    public ProductResponseDto crearProducto(CreateProductDto dto){

        Producto producto = new Producto(dto.getNombre(), dto.getPrecio(), dto.getStock());

        producto.setCategoria(obtenerCategoria(dto.getCategoriaId()));

        Producto guardado = repository.save(producto);
        return toResponse(guardado);
    }


    public ProductResponseDto actualizarProductoPorId(Long id, CreateProductDto dto){
        Producto producto = obtenerEntidad(id);
        producto.setNombre(dto.getNombre());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        producto.setCategoria(obtenerCategoria(dto.getCategoriaId()));
        Producto productoActualizado = repository.save(producto);

        return toResponse(productoActualizado);

    }

    public void eliminarProducto(Long id) {
        Producto producto = obtenerEntidad(id);
        repository.delete(producto);
    }

    private ProductResponseDto toResponse(Producto p) {
        CategoriaResponseDto categoriaDto = null;           // ← arranca en null
        if (p.getCategoria() != null) {                      // ← el chequeo clave
            categoriaDto = new CategoriaResponseDto(
                    p.getCategoria().getId(),
                    p.getCategoria().getNombre()
            );
        }
        return new ProductResponseDto(
                p.getId(), p.getNombre(), p.getPrecio(), p.getStock(), categoriaDto
        );
    }
}
