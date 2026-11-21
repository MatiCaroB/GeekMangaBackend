package GeekManga.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Se ejecuta en cada petición. Si viene "Authorization: Bearer <token>" y el token
 * es válido, deja al usuario autenticado (con su rol) para el resto de la petición.
 * Si no viene token o es inválido, la petición sigue como anónima y Spring Security
 * responde 401 en las rutas protegidas.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final String PREFIJO = "Bearer ";

    private final JwtService jwtService;
    private final UsuarioDetailsService usuarioDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header != null && header.startsWith(PREFIJO)
                && SecurityContextHolder.getContext().getAuthentication() == null) {

            String token = header.substring(PREFIJO.length());

            jwtService.validar(token, JwtService.TIPO_ACCESS).ifPresent(claims -> {
                try {
                    // Se vuelve a leer el usuario desde la BD: si fue eliminado o cambió de rol,
                    // el token antiguo deja de darle los permisos anteriores.
                    UsuarioPrincipal usuario =
                            (UsuarioPrincipal) usuarioDetailsService.loadUserByUsername(claims.getSubject());

                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                } catch (UsernameNotFoundException ignored) {
                    // Usuario ya no existe: se queda como anónimo
                }
            });
        }

        filterChain.doFilter(request, response);
    }
}
