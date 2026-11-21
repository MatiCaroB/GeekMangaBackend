package GeekManga.backend.producto;

import GeekManga.backend.categoria.Categoria;
import GeekManga.backend.categoria.CategoriaService;
import GeekManga.backend.error.ConflictoException;
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
    private final CategoriaService categoriaService;

    @Transactional
    public Producto agregarProducto(Producto producto) {
        productoRepository.findByCodigo(producto.getCodigo()).ifPresent(p -> {
            throw new ConflictoException("Ya existe un producto con el código " + producto.getCodigo());
        });
        producto.setId(null);
        producto.setCategoria(resolverCategoria(producto));
        validarOferta(producto);
        return productoRepository.save(producto);
    }

    @Transactional
    public Producto actualizarProducto(Long id, Producto datos) {
        Producto producto = buscarPorId(id);

        productoRepository.findByCodigo(datos.getCodigo()).ifPresent(otro -> {
            if (!otro.getId().equals(id)) {
                throw new ConflictoException("Ya existe otro producto con el código " + datos.getCodigo());
            }
        });

        producto.setCategoria(resolverCategoria(datos));
        producto.setCodigo(datos.getCodigo());
        producto.setProducto(datos.getProducto());
        producto.setDescripcion(datos.getDescripcion());
        producto.setMarca(datos.getMarca());
        producto.setPrecio(datos.getPrecio());
        producto.setOferta(datos.getOferta());
        producto.setPrecioOferta(datos.getPrecioOferta());
        producto.setStock(datos.getStock());
        producto.setStockCritico(datos.getStockCritico());
        producto.setUrlFoto(datos.getUrlFoto());
        validarOferta(producto);
        return productoRepository.save(producto);
    }

    @Transactional
    public void eliminarProducto(Long id) {
        Producto producto = buscarPorId(id);
        productoRepository.delete(producto);
        productoRepository.flush(); // fuerza el DELETE aquí para que un error de clave foránea se responda como 409
    }

    public Producto buscarPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se ha encontrado el producto"));
    }

    public Producto buscarPorNombre(String producto) {
        return productoRepository.findByProductoIgnoreCase(producto)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se ha encontrado el producto"));
    }

    public List<Producto> listarTodo() {
        return productoRepository.findAll();
    }

    public List<Producto> listarOfertas() {
        return productoRepository.findByOfertaTrue();
    }

    public List<Producto> listarPorCategoria(Long categoriaId) {
        categoriaService.buscarPorId(categoriaId); // 404 si la categoría no existe
        return productoRepository.findByCategoria_CategoriaId(categoriaId);
    }

    /** Se usa al crear una boleta: valida y descuenta el stock dentro de la misma transacción. */
    public Producto descontarStock(Long id, int cantidad) {
        Producto producto = buscarPorId(id);
        if (producto.getStock() < cantidad) {
            throw new StockInsuficienteException("No hay suficiente stock para el producto: " + producto.getProducto()
                    + " (disponible: " + producto.getStock() + ")");
        }
        producto.setStock(producto.getStock() - cantidad);
        return producto;
    }

    public int calcularPrecioUnitario(Producto producto) {
        if (producto.getOferta() && producto.getPrecioOferta() != null) {
            return producto.getPrecioOferta();
        }
        return producto.getPrecio();
    }

    /** El cliente solo manda { "categoriaId": X }; aquí se carga la categoría real desde la BD. */
    private Categoria resolverCategoria(Producto producto) {
        if (producto.getCategoria() == null || producto.getCategoria().getCategoriaId() == null) {
            throw new IllegalArgumentException("La categoría es obligatoria");
        }
        return categoriaService.buscarPorId(producto.getCategoria().getCategoriaId());
    }

    private void validarOferta(Producto producto) {
        if (!producto.getOferta()) {
            return;
        }
        if (producto.getPrecioOferta() == null) {
            throw new IllegalArgumentException("Si el producto está en oferta debe tener precio de oferta");
        }
        if (producto.getPrecioOferta() >= producto.getPrecio()) {
            throw new IllegalArgumentException("El precio de oferta debe ser menor al precio normal");
        }
    }
}
