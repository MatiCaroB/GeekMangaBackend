package GeekManga.backend.boleta;

import GeekManga.backend.detalle.DetalleBoleta;
import GeekManga.backend.dto.LineaPedido;
import GeekManga.backend.error.RecursoNoEncontradoException;
import GeekManga.backend.producto.Producto;
import GeekManga.backend.producto.ProductoService;
import GeekManga.backend.usuario.Usuario;
import GeekManga.backend.usuario.UsuarioService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BoletaService {

    private final BoletaRepository boletaRepository;
    private final ProductoService productoService;
    private final UsuarioService usuarioService;

    /**
     * Crea la boleta en una sola transacción: por cada producto valida y descuenta stock,
     * toma el precio vigente (oferta o normal) y calcula subtotales y total.
     * Si un producto no tiene stock suficiente se lanza StockInsuficienteException
     * y se revierte TODO (no queda stock descontado a medias).
     */
    @Transactional
    public Boleta crearBoleta(Long usuarioId, List<LineaPedido> items) {
        Usuario usuario = usuarioService.buscarPorId(usuarioId);

        Boleta boleta = new Boleta();
        boleta.setUsuario(usuario);
        boleta.setFechaEmision(LocalDateTime.now());

        int totalCalculado = 0;

        for (LineaPedido item : items) {
            Producto producto = productoService.descontarStock(item.productoId(), item.cantidad());
            int precioUnitario = productoService.calcularPrecioUnitario(producto);
            DetalleBoleta detalle = construirDetalle(boleta, producto, item.cantidad(), precioUnitario);

            boleta.getDetalles().add(detalle);
            totalCalculado += detalle.getSubtotal();
        }

        boleta.setTotal(totalCalculado);
        return boletaRepository.save(boleta);
    }

    public List<Boleta> listarTodo() {
        return boletaRepository.findAllByOrderByFechaEmisionDesc();
    }

    public List<Boleta> listarPorUsuario(Long usuarioId) {
        return boletaRepository.findByUsuario_UsuarioIdOrderByFechaEmisionDesc(usuarioId);
    }

    public Boleta buscarPorId(Long id) {
        return boletaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se ha encontrado la boleta"));
    }

    private DetalleBoleta construirDetalle(Boleta boleta, Producto producto, int cantidad, int precioUnitario) {
        DetalleBoleta detalle = new DetalleBoleta();
        detalle.setBoleta(boleta);
        detalle.setProducto(producto);
        detalle.setCantidad(cantidad);
        detalle.setPrecioUnitario(precioUnitario);
        detalle.setSubtotal(precioUnitario * cantidad);
        return detalle;
    }
}
