package GeekManga.backend.boleta;

import GeekManga.backend.boleta.dto.BoletaRequest;
import GeekManga.backend.security.UsuarioPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Boletas", description = "Compras de la tienda. Cualquier usuario logueado compra; ADMIN y VENDEDOR ven todas las boletas")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/boleta")
public class BoletaController {

    private final BoletaService boletaService;

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Crear boleta (pagar carrito)",
            description = "Recibe los productos y cantidades del carrito. El usuario se toma del token y los precios los calcula el servidor. Descuenta stock")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Boleta creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Carrito vacío o datos inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "404", description = "Algún producto no existe"),
            @ApiResponse(responseCode = "409", description = "Stock insuficiente")
    })
    @PostMapping
    public ResponseEntity<Boleta> crearBoleta(@Valid @RequestBody BoletaRequest request,
                                              @AuthenticationPrincipal UsuarioPrincipal usuario) {
        Boleta boleta = boletaService.crearBoleta(usuario.getUsuarioId(), request.items());
        return ResponseEntity.status(HttpStatus.CREATED).body(boleta);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Mis boletas", description = "Historial de compras del usuario dueño del token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Boletas encontradas correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    @GetMapping("/mis-boletas")
    public ResponseEntity<List<Boleta>> misBoletas(@AuthenticationPrincipal UsuarioPrincipal usuario) {
        return ResponseEntity.ok(boletaService.listarPorUsuario(usuario.getUsuarioId()));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    @Operation(summary = "Listar todas las boletas", description = "Todas las ventas de la tienda, de la más reciente a la más antigua")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Boletas encontradas correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos (solo ADMIN o VENDEDOR)")
    })
    @GetMapping
    public ResponseEntity<List<Boleta>> listarTodo() {
        return ResponseEntity.ok(boletaService.listarTodo());
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Buscar boleta por ID", description = "ADMIN y VENDEDOR ven cualquier boleta; un cliente solo las suyas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Boleta encontrada correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "La boleta no es tuya"),
            @ApiResponse(responseCode = "404", description = "Boleta no encontrada")
    })
    @GetMapping("/buscar/{id}")
    public ResponseEntity<Boleta> buscarPorId(@PathVariable Long id,
                                              @AuthenticationPrincipal UsuarioPrincipal usuario) {
        Boleta boleta = boletaService.buscarPorId(id);
        boolean puedeVerTodas = usuario.esAdmin() || "VENDEDOR".equals(usuario.getRol());
        if (!puedeVerTodas && !boleta.getUsuario().getUsuarioId().equals(usuario.getUsuarioId())) {
            throw new AccessDeniedException("Esta boleta no te pertenece");
        }
        return ResponseEntity.ok(boleta);
    }
}
