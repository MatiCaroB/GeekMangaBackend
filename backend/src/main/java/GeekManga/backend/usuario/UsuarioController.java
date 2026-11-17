package GeekManga.backend.usuario;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<Usuario> agregarUsuario(@Valid @RequestBody Usuario usuarios) {
        Usuario usuario = usuarioService.agregarUsuario(usuarios);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listarTodo() {
        List<Usuario> usuario = usuarioService.listarTodo();
        return ResponseEntity.ok(usuario);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscarid({id}")
    public ResponseEntity<Usuario> buscarUsuarioPorId(@PathVariable Long id){
        Usuario usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/actualizar({id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, Usuario usuarios){
        Usuario usuario = usuarioService.actualizarUsuario( id,  usuarios);
        return ResponseEntity.ok(usuario);
    }
}
