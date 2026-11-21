package GeekManga.backend.usuario;

import GeekManga.backend.security.UsuarioPrincipal;
import GeekManga.backend.usuario.dto.UsuarioRequest;
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

@Tag(name = "Usuarios", description = "Administración de usuarios (la creación de clientes se hace en /api/v1/auth/registro)")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear usuario", description = "El administrador crea un usuario con cualquier rol (ADMIN, VENDEDOR o USER)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos (solo ADMIN)"),
            @ApiResponse(responseCode = "409", description = "Correo o RUN ya registrados")
    })
    @PostMapping
    public ResponseEntity<Usuario> agregarUsuario(@Valid @RequestBody UsuarioRequest request) {
        Usuario usuario = usuarioService.crearDesdeAdmin(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar usuarios", description = "Devuelve todos los usuarios (sin contraseñas)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuarios encontrados correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos (solo ADMIN)")
    })
    @GetMapping
    public ResponseEntity<List<Usuario>> listarTodo() {
        return ResponseEntity.ok(usuarioService.listarTodo());
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Buscar usuario por ID", description = "El ADMIN puede ver cualquier usuario; los demás solo su propia cuenta")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/buscar/{id}")
    public ResponseEntity<Usuario> buscarUsuarioPorId(@PathVariable Long id,
                                                      @AuthenticationPrincipal UsuarioPrincipal actual) {
        verificarAdminOPropio(id, actual);
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Actualizar usuario",
            description = "El ADMIN puede editar a cualquiera y cambiar su rol; los demás solo editan su propia cuenta y no pueden cambiar su rol. Si password viene vacía se mantiene la actual")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "409", description = "Correo o RUN ya registrados")
    })
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id,
                                                     @Valid @RequestBody UsuarioRequest request,
                                                     @AuthenticationPrincipal UsuarioPrincipal actual) {
        verificarAdminOPropio(id, actual);
        return ResponseEntity.ok(usuarioService.actualizarUsuario(id, request, actual.esAdmin()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario. El administrador no puede eliminarse a sí mismo")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuario eliminado correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos (solo ADMIN)"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "409", description = "No se puede eliminar (por ejemplo, tiene boletas o es tu propia cuenta)")
    })
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id,
                                                @AuthenticationPrincipal UsuarioPrincipal actual) {
        if (id.equals(actual.getUsuarioId())) {
            throw new GeekManga.backend.error.ConflictoException("No puedes eliminar tu propia cuenta");
        }
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    private void verificarAdminOPropio(Long id, UsuarioPrincipal actual) {
        if (!actual.esAdmin() && !id.equals(actual.getUsuarioId())) {
            throw new AccessDeniedException("Solo puedes acceder a tu propia cuenta");
        }
    }
}
