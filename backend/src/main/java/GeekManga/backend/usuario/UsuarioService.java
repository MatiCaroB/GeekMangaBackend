package GeekManga.backend.usuario;


import GeekManga.backend.error.RecursoNoEncontradoException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UsuarioService {


    private final UsuarioRepository usuarioRepository;

    public Usuario agregarUsuario(Usuario usuario){
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarTodo(){
        return usuarioRepository.findAll();
    }

    public Usuario actualizarUsuario(Long id, Usuario usuario){
        return usuarioRepository.findById(id).map(usuario1 ->{
            usuario1.setNombre(usuario.getNombre());
            usuario1.setApellidos(usuario.getApellidos());
            usuario1.setCorreo(usuario.getCorreo());
            usuario1.setRun(usuario.getRun());
            usuario1.setPassword(usuario.getPassword());
            return usuarioRepository.save(usuario1);
        }).orElseThrow(() -> new RecursoNoEncontradoException("No se ha encontrado el usuario"));
    }

    public void eliminarUsuario(Long id){
         Usuario usuario = usuarioRepository.findById(id)
                 .orElseThrow(()-> new RecursoNoEncontradoException("No se ha encontrado el usuario"));
         usuarioRepository.delete(usuario);
    }

    public Usuario buscarPorId(Long id){
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("EL usuario no se ha encontrado"));
    }

}
