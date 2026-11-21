package GeekManga.backend.producto;

import GeekManga.backend.error.RecursoNoEncontradoException;
import GeekManga.backend.error.StockInsuficienteException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    @Transactional
    public Producto agregarProducto (Producto producto){
        return productoRepository.save(producto);
    }

    public Producto actualizarProducto(Long id, Producto producto){
        return productoRepository.findById(id).map(producto1 -> {
            producto1.setCategoria(producto.getCategoria());
            producto1.setCodigo(producto.getCodigo());
            producto1.setDescripcion(producto.getDescripcion());
            producto1.setMarca(producto.getMarca());
            producto1.setPrecio(producto.getPrecio());
            producto1.setPrecioOferta(producto.getPrecioOferta());
            producto1.setProducto(producto.getProducto());
            producto1.setStock(producto.getStock());
            producto1.setStockCritico(producto.getStockCritico());
            producto1.setUrlFoto(producto.getUrlFoto());
            return productoRepository.save(producto1);
        }).orElseThrow(() -> new RecursoNoEncontradoException("No se ha encontrado el producto"));
    }

    public void eliminarProducto(Long id){
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se ha encontrado el producto"));
        productoRepository.delete(producto);
    }

    public Producto buscarPorId(Long id){
        return productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se ha encontrado el producto"));
    }

    public Producto buscarPorNombre(String producto){
        return productoRepository.findByProducto(producto)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se ha encontrado el producto"));
    }

    public List<Producto> listarTodo(){
        return productoRepository.findAll();
    }

    public Producto descontarStock( Long id, int cantidad){
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("El producto no se ha encontrado"));

        if (producto.getStock() < cantidad) {
            throw new StockInsuficienteException("No hay suficiente stock para el producto: " + producto.getProducto());
        }

        producto.setStock(producto.getStock() - cantidad);
        return producto;
    }

    public int calcularPrecioUnitario(Producto producto) {
        if (producto.isOferta() && producto.getPrecioOferta() != null){
            return producto.getPrecioOferta();
        }
        return producto.getPrecio();
    }

}
