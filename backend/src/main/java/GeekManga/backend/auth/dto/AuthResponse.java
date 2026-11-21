package GeekManga.backend.auth.dto;

/**
 * Respuesta del login / registro / refresh.
 * El frontend guarda token y refreshToken, y usa "usuario" para saber qué mostrar según el rol.
 */
public record AuthResponse(
        String token,
        String refreshToken,
        String tipo,
        long expiraEnMs,
        UsuarioResumen usuario) {

    public record UsuarioResumen(Long usuarioId, String nombre, String correo, String rol) {
    }
}
