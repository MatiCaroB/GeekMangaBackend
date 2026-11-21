package GeekManga.backend.categoria;

import GeekManga.backend.error.ConflictoException;
import GeekManga.backend.error.RecursoNoEncontradoException;
import GeekManga.backend.producto.ProductoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;

    @Transactional
    public Categoria agregarCategoria(Categoria categoria) {
        validarNombreUnico(categoria.getNombreCategoria(), null);
        categoria.setCategoriaId(null);
        return categoriaRepository.save(categoria);
    }

    @Transactional
    public Categoria actualizarCategoria(Long id, Categoria categoria) {
        Categoria existente = buscarPorId(id);
        validarNombreUnico(categoria.getNombreCategoria(), id);
        existente.setNombreCategoria(categoria.getNombreCategoria());
        return categoriaRepository.save(existente);
    }

    /** No se permite borrar una categoría que todavía tiene productos asociados. */
    @Transactional
    public void eliminarCategoria(Long id) {
        Categoria categoria = buscarPorId(id);
        if (productoRepository.existsByCategoria_CategoriaId(id)) {
            throw new ConflictoException("No se puede eliminar la categoría porque tiene productos asociados");
        }
        categoriaRepository.delete(categoria);
        categoriaRepository.flush(); // fuerza el DELETE aquí para que un error de clave foránea se responda como 409
    }

    public Categoria buscarPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se ha encontrado la categoría"));
    }

    public Categoria buscarPorNombre(String nombreCategoria) {
        return categoriaRepository.findByNombreCategoriaIgnoreCase(nombreCategoria)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se ha encontrado la categoría"));
    }

    public List<Categoria> listarTodo() {
        return categoriaRepository.findAll();
    }

    private void validarNombreUnico(String nombre, Long idActual) {
        categoriaRepository.findByNombreCategoriaIgnoreCase(nombre).ifPresent(c -> {
            if (!c.getCategoriaId().equals(idActual)) {
                throw new ConflictoException("Ya existe la categoría " + nombre);
            }
        });
    }
}
