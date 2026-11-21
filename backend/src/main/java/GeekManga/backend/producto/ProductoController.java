package GeekManga.backend.producto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Productos", description = "Catálogo de la tienda: lectura pública; creación, edición y eliminación solo ADMIN")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/producto")
public class ProductoController {

    private final ProductoService productoService;

    @SecurityRequirements
    @Operation(summary = "Busca los productos", description = "Devuelve todos los productos del sistema (público)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Productos encontrados correctamente")
    })
    @GetMapping
    public ResponseEntity<List<Producto>> listarTodo() {
        return ResponseEntity.ok(productoService.listarTodo());
    }

    @SecurityRequirements
    @Operation(summary = "Productos en oferta", description = "Devuelve solo los productos con oferta activa (público)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Productos encontrados correctamente")
    })
    @GetMapping("/ofertas")
    public ResponseEntity<List<Producto>> listarOfertas() {
        return ResponseEntity.ok(productoService.listarOfertas());
    }

    @SecurityRequirements
    @Operation(summary = "Productos por categoría", description = "Devuelve los productos de una categoría (público)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Productos encontrados correctamente"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<Producto>> listarPorCategoria(@PathVariable Long categoriaId) {
        return ResponseEntity.ok(productoService.listarPorCategoria(categoriaId));
    }

    @SecurityRequirements
    @Operation(summary = "Busca un producto por ID", description = "Devuelve un producto por su id (público)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto encontrado correctamente"),
            @ApiResponse(responseCode = "400", description = "ID inválido"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/buscar/{id}")
    public ResponseEntity<Producto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.buscarPorId(id));
    }

    @SecurityRequirements
    @Operation(summary = "Busca un producto por nombre", description = "Devuelve un producto por su nombre, sin distinguir mayúsculas (público)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto encontrado correctamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/buscar/nombre/{producto}")
    public ResponseEntity<Producto> buscarPorNombre(@PathVariable String producto) {
        return ResponseEntity.ok(productoService.buscarPorNombre(producto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Agrega un producto",
            description = "Agrega un producto al catálogo. La categoría se envía como { \"categoriaId\": 1 }")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Producto agregado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos (solo ADMIN)"),
            @ApiResponse(responseCode = "404", description = "La categoría indicada no existe"),
            @ApiResponse(responseCode = "409", description = "Ya existe un producto con ese código")
    })
    @PostMapping
    public ResponseEntity<Producto> agregarProducto(@Valid @RequestBody Producto producto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.agregarProducto(producto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar producto", description = "Modifica los datos de un producto existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos (solo ADMIN)"),
            @ApiResponse(responseCode = "404", description = "Producto o categoría no encontrados"),
            @ApiResponse(responseCode = "409", description = "Ya existe otro producto con ese código")
    })
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Long id, @Valid @RequestBody Producto producto) {
        return ResponseEntity.ok(productoService.actualizarProducto(id, producto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar producto", description = "Elimina un producto del catálogo")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Producto eliminado correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos (solo ADMIN)"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "409", description = "El producto aparece en boletas y no se puede eliminar")
    })
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
}
