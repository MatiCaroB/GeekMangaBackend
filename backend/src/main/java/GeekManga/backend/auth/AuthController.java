package GeekManga.backend.auth;

import GeekManga.backend.auth.dto.AuthResponse;
import GeekManga.backend.auth.dto.LoginRequest;
import GeekManga.backend.auth.dto.RefreshRequest;
import GeekManga.backend.auth.dto.RegistroRequest;
import GeekManga.backend.security.UsuarioPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Autenticación", description = "Login, registro y renovación de tokens JWT")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @SecurityRequirements // público: no requiere token
    @Operation(summary = "Iniciar sesión", description = "Valida correo y contraseña y devuelve un access token y un refresh token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login correcto"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "Correo o contraseña incorrectos")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @SecurityRequirements
    @Operation(summary = "Registrarse", description = "Crea una cuenta de cliente (rol USER) y devuelve sus tokens")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario registrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "409", description = "El correo o el RUN ya están registrados")
    })
    @PostMapping("/registro")
    public ResponseEntity<AuthResponse> registrar(@Valid @RequestBody RegistroRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registrar(request));
    }

    @SecurityRequirements
    @Operation(summary = "Renovar token", description = "Recibe el refresh token y entrega un access token nuevo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Token renovado"),
            @ApiResponse(responseCode = "401", description = "Refresh token inválido o expirado")
    })
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refrescar(@Valid @RequestBody RefreshRequest request) {
        return ResponseEntity.ok(authService.refrescar(request.refreshToken()));
    }

    @Operation(summary = "Usuario actual", description = "Devuelve los datos del usuario dueño del token (sirve para restaurar la sesión al recargar)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Datos del usuario"),
            @ApiResponse(responseCode = "401", description = "Token ausente, inválido o expirado")
    })
    @GetMapping("/me")
    public ResponseEntity<AuthResponse.UsuarioResumen> yo(@AuthenticationPrincipal UsuarioPrincipal usuario) {
        return ResponseEntity.ok(authService.resumen(usuario));
    }
}
