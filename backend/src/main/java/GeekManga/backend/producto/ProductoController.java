package GeekManga.backend.producto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/producto")
public class ProductoController {

    private final ProductoService productoService;


    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(
            summary = "Busca los productos",
            description = "Busca y devuelve todos los productos que hayan en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Productos encontrados correctamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autenticado"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Sin permisos"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Recurso no encontrado"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Fallo del servidor"
            )



    })
    @GetMapping
    public ResponseEntity<List<Producto>> listarTodo(){
        List<Producto> productos = productoService.listarTodo();
        return ResponseEntity.ok(productos);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(
            summary = "actualizar producto",
            description = "Modifica los datos de un producto en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Producto actualizado correctamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autenticado"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Sin permisos"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Recurso no encontrado"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Fallo del servidor"
            )



    })
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Long id, @Valid @RequestBody Producto productos){
        Producto producto = productoService.actualizarProducto(id, productos);
        return ResponseEntity.ok(producto);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(
            summary = "eliminar producto",
            description = "Elimina un producto que se encuentre en el sustema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Producto eliminado correctamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autenticado"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Sin permisos"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Recurso no encontrado"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Fallo del servidor"
            )



    })
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id){
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }


    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(
            summary = "Agrega un producto",
            description = "Agrega un producto al sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Producto agregado correctamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autenticado"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Sin permisos"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Recurso no encontrado"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Fallo del servidor"
            )



    })
    @PostMapping
    public ResponseEntity<Producto> agregarProducto(@Valid @RequestBody Producto productos){
        Producto producto = productoService.agregarProducto(productos);
        return ResponseEntity.status(HttpStatus.CREATED).body(producto);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Operation(
            summary = "Busca un producto",
            description = "Busca y devuelve un producto por su id"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Producto encontrado correctamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autenticado"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Sin permisos"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Recurso no encontrado"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Fallo del servidor"
            )



    })
    @GetMapping("/buscar/{id}")
    public ResponseEntity<Producto> buscarPorId(@PathVariable Long id){
        Producto producto = productoService.buscarPorId(id);
        return ResponseEntity.ok(producto);
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(
            summary = "Busca un producto",
            description = "Busca y devuelve un producto por su nombre"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Producto encontrado correctamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autenticado"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Sin permisos"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Recurso no encontrado"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Fallo del servidor"
            )



    })
    @GetMapping("/buscar/nombre/{producto}")
    public ResponseEntity<Producto> buscarPorNombre(@PathVariable String producto){
        Producto productos = productoService.buscarPorNombre(producto);
        return ResponseEntity.ok(productos);
    }
}
