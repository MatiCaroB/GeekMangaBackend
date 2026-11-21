package GeekManga.backend.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Datos que envía el formulario de registro de la tienda.
 * No incluye el rol: todo usuario que se registra queda como USER (cliente).
 */
public record RegistroRequest(
        @NotBlank(message = "Se necesita el nombre") @Size(max = 25) String nombre,
        @Size(max = 30) String apellidos,
        @NotBlank(message = "El rut es necesario") @Size(max = 10) String run,
        @NotBlank(message = "El correo es obligatorio") @Email(message = "Formato de correo inválido") String correo,
        @NotBlank(message = "La contraseña es obligatoria") @Size(min = 4, max = 100, message = "La contraseña debe tener al menos 4 caracteres") String password) {
}
