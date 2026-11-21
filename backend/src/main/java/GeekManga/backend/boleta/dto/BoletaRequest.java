package GeekManga.backend.boleta.dto;

import GeekManga.backend.dto.LineaPedido;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/**
 * Lo que envía el frontend al pagar: solo productos y cantidades.
 * El usuario se obtiene del token JWT y los precios los calcula el servidor.
 */
public record BoletaRequest(
        @NotEmpty(message = "El carrito está vacío") List<@Valid LineaPedido> items) {
}
