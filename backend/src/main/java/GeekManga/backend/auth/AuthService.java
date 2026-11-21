package GeekManga.backend.auth;

import GeekManga.backend.auth.dto.AuthResponse;
import GeekManga.backend.auth.dto.LoginRequest;
import GeekManga.backend.auth.dto.RegistroRequest;
import GeekManga.backend.error.TokenInvalidoException;
import GeekManga.backend.security.JwtService;
import GeekManga.backend.security.UsuarioDetailsService;
import GeekManga.backend.security.UsuarioPrincipal;
import GeekManga.backend.usuario.Rol;
import GeekManga.backend.usuario.Usuario;
import GeekManga.backend.usuario.UsuarioService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioService usuarioService;
    private final UsuarioDetailsService usuarioDetailsService;

    /** Verifica correo + contraseña (BCrypt) y entrega los tokens. Si falla lanza BadCredentialsException (401). */
    public AuthResponse login(LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.correo().trim().toLowerCase(), request.password()));
        return crearRespuesta((UsuarioPrincipal) auth.getPrincipal());
    }

    /** Crea un cliente (rol USER) y lo deja con sesión iniciada. */
    public AuthResponse registrar(RegistroRequest request) {
        Usuario usuario = new Usuario();
        usuario.setNombre(request.nombre());
        usuario.setApellidos(request.apellidos());
        usuario.setRun(request.run());
        usuario.setCorreo(request.correo());
        usuario.setPassword(request.password());
        usuario.setTipoUsuario(Rol.USER);

        Usuario guardado = usuarioService.agregarUsuario(usuario);
        return crearRespuesta(new UsuarioPrincipal(guardado));
    }

    /**
     * Renovación del token: recibe un refresh token válido y entrega un access token nuevo
     * (y un refresh token nuevo). Los datos del usuario se leen otra vez desde la BD.
     */
    public AuthResponse refrescar(String refreshToken) {
        Claims claims = jwtService.validar(refreshToken, JwtService.TIPO_REFRESH)
                .orElseThrow(() -> new TokenInvalidoException("El refresh token es inválido o expiró"));
        try {
            UsuarioPrincipal usuario = (UsuarioPrincipal) usuarioDetailsService.loadUserByUsername(claims.getSubject());
            return crearRespuesta(usuario);
        } catch (UsernameNotFoundException e) {
            throw new TokenInvalidoException("El usuario del token ya no existe");
        }
    }

    public AuthResponse.UsuarioResumen resumen(UsuarioPrincipal usuario) {
        return new AuthResponse.UsuarioResumen(
                usuario.getUsuarioId(), usuario.getNombre(), usuario.getUsername(), usuario.getRol());
    }

    private AuthResponse crearRespuesta(UsuarioPrincipal usuario) {
        return new AuthResponse(
                jwtService.generarAccessToken(usuario),
                jwtService.generarRefreshToken(usuario),
                "Bearer",
                jwtService.getExpiracionMs(),
                resumen(usuario));
    }
}
