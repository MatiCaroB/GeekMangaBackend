package GeekManga.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

/**
 * Genera y valida los tokens JWT.
 *
 * - Access token: corta duración (jwt.expiration-ms). Se envía en cada petición
 *   en el header "Authorization: Bearer <token>".
 * - Refresh token: larga duración (jwt.refresh-expiration-ms). Solo sirve para
 *   pedir un access token nuevo en /api/v1/auth/refresh.
 *
 * Los tokens se firman con HMAC-SHA256 usando la clave jwt.secret, así que si
 * alguien modifica el token (por ejemplo, cambia el rol) la firma deja de ser válida.
 */
@Service
public class JwtService {

    public static final String TIPO_ACCESS = "access";
    public static final String TIPO_REFRESH = "refresh";

    private final SecretKey clave;
    private final long expiracionMs;
    private final long expiracionRefreshMs;

    public JwtService(@Value("${jwt.secret}") String secret,
                      @Value("${jwt.expiration-ms}") long expiracionMs,
                      @Value("${jwt.refresh-expiration-ms}") long expiracionRefreshMs) {
        this.clave = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.expiracionMs = expiracionMs;
        this.expiracionRefreshMs = expiracionRefreshMs;
    }

    public String generarAccessToken(UsuarioPrincipal usuario) {
        return construirToken(usuario, TIPO_ACCESS, expiracionMs);
    }

    public String generarRefreshToken(UsuarioPrincipal usuario) {
        return construirToken(usuario, TIPO_REFRESH, expiracionRefreshMs);
    }

    public long getExpiracionMs() {
        return expiracionMs;
    }

    private String construirToken(UsuarioPrincipal usuario, String tipo, long duracionMs) {
        Date ahora = new Date();
        return Jwts.builder()
                .subject(usuario.getUsername())
                .claim("id", usuario.getUsuarioId())
                .claim("nombre", usuario.getNombre())
                .claim("rol", usuario.getRol())
                .claim("tipo", tipo)
                .issuedAt(ahora)
                .expiration(new Date(ahora.getTime() + duracionMs))
                .signWith(clave)
                .compact();
    }

    /**
     * Valida firma y expiración. Devuelve los datos del token si es válido
     * y es del tipo esperado; si no, devuelve vacío.
     */
    public Optional<Claims> validar(String token, String tipoEsperado) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(clave)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            if (!tipoEsperado.equals(claims.get("tipo", String.class))) {
                return Optional.empty();
            }
            return Optional.of(claims);
        } catch (JwtException | IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
