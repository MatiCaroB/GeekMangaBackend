package GeekManga.backend.categoria;

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
@RequestMapping("/api/v1/categoria")
public class CategoriaController {

    private final CategoriaService categoriaService;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(
            summary = "Busca las categorías",
            description = "Busca y devuelve todas las categorías que hayan en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categorías encontradas correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos"),
            @ApiResponse(responseCode = "404", description = "Recurso no encontrado"),
            @ApiResponse(responseCode = "500", description = "Fallo del servidor")
    })
    @GetMapping
    public ResponseEntity<List<Categoria>> listarTodo() {
        List<Categoria> categorias = categoriaService.listarTodo();
        return ResponseEntity.ok(categorias);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(
            summary = "Actualizar categoría",
            description = "Modifica los datos de una categoría en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos"),
            @ApiResponse(responseCode = "404", description = "Recurso no encontrado"),
            @ApiResponse(responseCode = "500", description = "Fallo del servidor")
    })
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Categoria> actualizarCategoria(@PathVariable Long id, @Valid @RequestBody Categoria categoria) {
        Categoria categoriaActualizada = categoriaService.actualizarCategoria(id, categoria);
        return ResponseEntity.ok(categoriaActualizada);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(
            summary = "Eliminar categoría",
            description = "Elimina una categoría que se encuentre en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Categoría eliminada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos"),
            @ApiResponse(responseCode = "404", description = "Recurso no encontrado"),
            @ApiResponse(responseCode = "500", description = "Fallo del servidor")
    })
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id) {
        categoriaService.eliminarCategoria(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(
            summary = "Agrega una categoría",
            description = "Agrega una categoría al sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoría agregada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos"),
            @ApiResponse(responseCode = "404", description = "Recurso no encontrado"),
            @ApiResponse(responseCode = "500", description = "Fallo del servidor")
    })
    @PostMapping
    public ResponseEntity<Categoria> agregarCategoria(@Valid @RequestBody Categoria categoria) {
        Categoria categoriaGuardada = categoriaService.agregarCategoria(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaGuardada);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(
            summary = "Busca una categoría por ID",
            description = "Busca y devuelve una categoría por su id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría encontrada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos"),
            @ApiResponse(responseCode = "404", description = "Recurso no encontrado"),
            @ApiResponse(responseCode = "500", description = "Fallo del servidor")
    })
    @GetMapping("/buscar/{id}")
    public ResponseEntity<Categoria> buscarPorId(@PathVariable Long id) {
        Categoria categoria = categoriaService.buscarPorId(id);
        return ResponseEntity.ok(categoria);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @Operation(
            summary = "Busca una categoría por nombre",
            description = "Busca y devuelve una categoría por su nombre"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría encontrada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos"),
            @ApiResponse(responseCode = "404", description = "Recurso no encontrado"),
            @ApiResponse(responseCode = "500", description = "Fallo del servidor")
    })
    @GetMapping("/buscar/nombre/{nombreCategoria}")
    public ResponseEntity<Categoria> buscarPorNombre(@PathVariable String nombreCategoria) {
        Categoria categoria = categoriaService.buscarPorNombre(nombreCategoria);
        return ResponseEntity.ok(categoria);
    }
}
