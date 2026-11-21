package GeekManga.backend.security;

import GeekManga.backend.usuario.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Usuario autenticado tal como lo ve Spring Security.
 * Guarda el id y el rol para no tener que volver a consultar la base de datos en los controllers.
 */
public class UsuarioPrincipal implements UserDetails {

    private final Long usuarioId;
    private final String correo;
    private final String password;
    private final String nombre;
    private final String rol;

    public UsuarioPrincipal(Usuario usuario) {
        this.usuarioId = usuario.getUsuarioId();
        this.correo = usuario.getCorreo();
        this.password = usuario.getPassword();
        this.nombre = usuario.getNombre();
        this.rol = usuario.getTipoUsuario().name();
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public String getNombre() {
        return nombre;
    }

    public String getRol() {
        return rol;
    }

    public boolean esAdmin() {
        return "ADMIN".equals(rol);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return correo;
    }
}
