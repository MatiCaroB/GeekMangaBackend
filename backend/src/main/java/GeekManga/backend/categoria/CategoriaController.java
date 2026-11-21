package GeekManga.backend.categoria;

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

@Tag(name = "Categorías", description = "Lectura pública; creación, edición y eliminación solo ADMIN")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/categoria")
public class CategoriaController {

    private final CategoriaService categoriaService;

    @SecurityRequirements
    @Operation(summary = "Busca las categorías", description = "Devuelve todas las categorías del sistema (público)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categorías encontradas correctamente")
    })
    @GetMapping
    public ResponseEntity<List<Categoria>> listarTodo() {
        return ResponseEntity.ok(categoriaService.listarTodo());
    }

    @SecurityRequirements
    @Operation(summary = "Busca una categoría por ID", description = "Devuelve una categoría por su id (público)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoría encontrada correctamente"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @GetMapping("/buscar/{id}")
    public ResponseEntity<Categoria> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaService.buscarPorId(id));
    }

    @SecurityRequirements
    @Operation(summary = "Busca una categoría por nombre", description = "Devuelve una categoría por su nombre, sin distinguir mayúsculas (público)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoría encontrada correctamente"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @GetMapping("/buscar/nombre/{nombreCategoria}")
    public ResponseEntity<Categoria> buscarPorNombre(@PathVariable String nombreCategoria) {
        return ResponseEntity.ok(categoriaService.buscarPorNombre(nombreCategoria));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Agrega una categoría", description = "Agrega una categoría al sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Categoría agregada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos (solo ADMIN)"),
            @ApiResponse(responseCode = "409", description = "La categoría ya existe")
    })
    @PostMapping
    public ResponseEntity<Categoria> agregarCategoria(@Valid @RequestBody Categoria categoria) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.agregarCategoria(categoria));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar categoría", description = "Modifica el nombre de una categoría")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoría actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos (solo ADMIN)"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
            @ApiResponse(responseCode = "409", description = "Ya existe otra categoría con ese nombre")
    })
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Categoria> actualizarCategoria(@PathVariable Long id, @Valid @RequestBody Categoria categoria) {
        return ResponseEntity.ok(categoriaService.actualizarCategoria(id, categoria));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar categoría", description = "Elimina una categoría que no tenga productos asociados")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Categoría eliminada correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos (solo ADMIN)"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
            @ApiResponse(responseCode = "409", description = "La categoría tiene productos asociados")
    })
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id) {
        categoriaService.eliminarCategoria(id);
        return ResponseEntity.noContent().build();
    }
}
