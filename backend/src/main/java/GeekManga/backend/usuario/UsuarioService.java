package GeekManga.backend.usuario;

import GeekManga.backend.error.ConflictoException;
import GeekManga.backend.error.RecursoNoEncontradoException;
import GeekManga.backend.usuario.dto.UsuarioRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    /** Guarda un usuario nuevo: normaliza el correo, valida duplicados y encripta la contraseña. */
    @Transactional
    public Usuario agregarUsuario(Usuario usuario) {
        usuario.setCorreo(normalizarCorreo(usuario.getCorreo()));
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            throw new ConflictoException("El correo " + usuario.getCorreo() + " ya está registrado");
        }
        if (usuarioRepository.existsByRun(usuario.getRun())) {
            throw new ConflictoException("El RUN " + usuario.getRun() + " ya está registrado");
        }
        if (usuario.getTipoUsuario() == null) {
            usuario.setTipoUsuario(Rol.USER);
        }
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }

    /** Creación desde el panel de administración (permite elegir el rol). */
    @Transactional
    public Usuario crearDesdeAdmin(UsuarioRequest request) {
        if (request.password() == null || request.password().isBlank()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }
        Usuario usuario = new Usuario();
        usuario.setNombre(request.nombre());
        usuario.setApellidos(request.apellidos());
        usuario.setRun(request.run());
        usuario.setCorreo(request.correo());
        usuario.setPassword(request.password());
        usuario.setTipoUsuario(request.tipoUsuario() == null ? Rol.USER : request.tipoUsuario());
        return agregarUsuario(usuario);
    }

    public List<Usuario> listarTodo() {
        return usuarioRepository.findAll();
    }

    /**
     * Actualiza datos del usuario.
     * @param puedeCambiarRol solo true cuando quien edita es ADMIN.
     */
    @Transactional
    public Usuario actualizarUsuario(Long id, UsuarioRequest request, boolean puedeCambiarRol) {
        Usuario usuario = buscarPorId(id);

        String correoNuevo = normalizarCorreo(request.correo());
        if (!correoNuevo.equals(usuario.getCorreo()) && usuarioRepository.existsByCorreo(correoNuevo)) {
            throw new ConflictoException("El correo " + correoNuevo + " ya está registrado");
        }
        if (!request.run().equals(usuario.getRun()) && usuarioRepository.existsByRun(request.run())) {
            throw new ConflictoException("El RUN " + request.run() + " ya está registrado");
        }

        usuario.setNombre(request.nombre());
        usuario.setApellidos(request.apellidos());
        usuario.setCorreo(correoNuevo);
        usuario.setRun(request.run());
        if (request.password() != null && !request.password().isBlank()) {
            usuario.setPassword(passwordEncoder.encode(request.password()));
        }
        if (puedeCambiarRol && request.tipoUsuario() != null) {
            usuario.setTipoUsuario(request.tipoUsuario());
        }
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void eliminarUsuario(Long id) {
        Usuario usuario = buscarPorId(id);
        usuarioRepository.delete(usuario);
        usuarioRepository.flush(); // fuerza el DELETE aquí para que un error de clave foránea se responda como 409
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("El usuario no se ha encontrado"));
    }

    private String normalizarCorreo(String correo) {
        return correo == null ? null : correo.trim().toLowerCase();
    }
}
