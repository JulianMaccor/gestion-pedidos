package com.ejemplo.pedidos.service;
import com.ejemplo.pedidos.dto.CreateProductDto;
import com.ejemplo.pedidos.exception.ProductoNoEncontradoException;
import com.ejemplo.pedidos.model.Producto;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductoService {

    private List<Producto> productos;

    public ProductoService() {
        productos = new ArrayList<>();
        productos.add(new Producto(103L, "Placa Universal Audio", 900, 19));
        productos.add(new Producto(104L, "Consola DDJ400 Pioneer", 600, 1));
    }

    public List<Producto> listarProductos(){
        return productos;
    }

    public Producto buscarPorId(Long id){
        return productos.stream()
                .filter(producto -> producto.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ProductoNoEncontradoException(id));
    }


    public Producto crearProducto(CreateProductDto dto){
        Long nuevoId = (long) (productos.size() + 1);
        Producto producto = new Producto(nuevoId, dto.getNombre(), dto.getPrecio(), dto.getStock());
        productos.add(producto);
        return producto;
    }
}
