package GeekManga.backend.usuario.dto;

import GeekManga.backend.usuario.Rol;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Datos para crear o editar un usuario desde el panel de administración.
 * - password: obligatoria al crear; al editar, si viene vacía se mantiene la actual.
 * - tipoUsuario: solo el ADMIN puede asignarlo. Si no viene, queda USER.
 */
public record UsuarioRequest(
        @NotBlank(message = "Se necesita el nombre") @Size(max = 25) String nombre,
        @Size(max = 30) String apellidos,
        @NotBlank(message = "El rut es necesario") @Size(max = 10) String run,
        @NotBlank(message = "El correo es obligatorio") @Email(message = "Formato de correo inválido") String correo,
        @Size(min = 4, max = 100, message = "La contraseña debe tener al menos 4 caracteres") String password,
        @Schema(example = "USER") Rol tipoUsuario) {
}
