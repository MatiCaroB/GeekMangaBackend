package GeekManga.backend.usuario;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuarios")
@Schema(description = "Entidad que representa a un usuario registrado en el sistema")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del usuario para gestión interna del sistema",
            example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long usuarioId;

    @Column(nullable = false, length = 25)
    @NotBlank(message = "Se necesita el nombre")
    @Size(min = 1, max = 25)
    @Schema(description = "Nombre del usuario", example = "Carlos")
    private String nombre;

    @Column(length = 30)
    @Size(max = 30)
    @Schema(description = "Apellido/s del usuario", example = "Gonzalez",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String apellidos;

    @Column(nullable = false, unique = true, length = 10)
    @NotBlank(message = "El rut es necesario")
    @Size(max = 10)
    @Schema(description = "RUN sin puntos y con guion", example = "12345678-9")
    private String run;

    @Column(nullable = false, unique = true, length = 100)
    @NotBlank(message = "El correo es obligatorio")
    @Email
    @Schema(description = "Correo con el que el usuario inicia sesión", example = "carlos@gmail.com")
    private String correo;

    /** Se guarda encriptada con BCrypt y NUNCA se devuelve en las respuestas JSON. */
    @Column(nullable = false)
    @NotBlank(message = "La contraseña es obligatoria")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "Contraseña (solo escritura)", accessMode = Schema.AccessMode.WRITE_ONLY)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_usuario", nullable = false, length = 20)
    @Schema(description = "Rol del usuario", example = "USER")
    private Rol tipoUsuario = Rol.USER;

    /** Por si hay registros antiguos sin rol en la base de datos. */
    public Rol getTipoUsuario() {
        return tipoUsuario == null ? Rol.USER : tipoUsuario;
    }
}
