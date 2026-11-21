package GeekManga.backend.usuario;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuarios")
@Schema(description = "Entidad que representa a un usuario registrado en el sistema")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del usuario para gestón interno del sistema técnico",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED, accessMode = Schema.AccessMode.READ_ONLY)
    private Long usuarioId;

    @Column(nullable = false)
    @NotBlank(message = "Se necesita el nombre")
    @Size(min= 1, max = 25)
    @Schema(description = "Nombre del usuario",
            example = "Carlos",
            requiredMode = Schema.RequiredMode.REQUIRED, accessMode = Schema.AccessMode.READ_WRITE)
    private String nombre;

    @Size(min = 1, max = 30)
    @Schema(description = "Apellido/s del usuario",
            example = "Gonzalez",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED, accessMode = Schema.AccessMode.READ_WRITE)
    private String apellidos;

    @Column(nullable = false)
    @NotBlank(message = "El rut es necesario")
    @Size(max = 10)
    private String run;

    @Column(nullable = false)
    @NotBlank(message = "El correo es obligatorio")
    @Email
    private String correo;

    @Column(nullable = false)
    @NotBlank(message = "La contraseña es obligatoria")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;


    private String tipoUsuario;
}
