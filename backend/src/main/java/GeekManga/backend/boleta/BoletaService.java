package GeekManga.backend.boleta;


import GeekManga.backend.detalle.DetalleBoleta;
import GeekManga.backend.dto.LineaPedido;
import GeekManga.backend.producto.Producto;
import GeekManga.backend.producto.ProductoRepository;
import GeekManga.backend.producto.ProductoService;
import GeekManga.backend.usuario.Usuario;
import GeekManga.backend.usuario.UsuarioRepository;
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
    private final ProductoRepository productoRepository;
    private final ProductoService productoService;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;

    @Transactional
    public Boleta crearBoleta(Long usuarioId, List<LineaPedido> items){
        Usuario usuario = usuarioService.buscarPorId(usuarioId);

        Boleta boleta = new Boleta();
        boleta.setUsuario(usuario);
        boleta.setFechaEmision(LocalDateTime.now());

        int totalCalculado = 0;

        for (LineaPedido item : items ){
            Producto producto = productoService.descontarStock(item.productoId(),item.cantidad());
            int precioUnitario = productoService.calcularPrecioUnitario(producto);
            DetalleBoleta detalle = construirDetalle(boleta, producto, item.cantidad(), precioUnitario);

            boleta.getDetalles().add(detalle);
            totalCalculado += detalle.getSubtotal();
        }

        boleta.setTotal(totalCalculado);
        return boletaRepository.save(boleta);
    }


    public DetalleBoleta construirDetalle(Boleta boleta, Producto producto, int cantidad, int precioUnitario) {
        DetalleBoleta detalle = new DetalleBoleta();
        detalle.setBoleta(boleta);
        detalle.setProducto(producto);
        detalle.setCantidad(cantidad);
        detalle.setPrecioUnitario(precioUnitario);
        detalle.setSubtotal(precioUnitario * cantidad);
        return detalle;
    }


}
