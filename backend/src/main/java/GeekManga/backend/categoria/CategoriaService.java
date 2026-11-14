package GeekManga.backend.categoria;


import GeekManga.backend.categoria.CategoriaRepository;
import GeekManga.backend.error.RecursoNoEncontradoException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Transactional
    public Categoria agregarCategoria (Categoria categoria){
        return categoriaRepository.save(categoria);
    }

    public Categoria actualizarCategoria(Long id, Categoria categoria){
        return categoriaRepository.findById(id).map(categoria1 -> {
            categoria1.setNombreCategoria(categoria.getNombreCategoria());
            return categoriaRepository.save(categoria1);
        }).orElseThrow(() -> new RecursoNoEncontradoException("No se ha encontrado la Categoria"));
    }

    public void eliminarCategoria(Long id){
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se ha encontrado la Categoria"));
        categoriaRepository.delete(categoria);
    }

    public Categoria buscarPorId(Long id){
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se ha encontrado la Categoria"));
    }

    public Categoria buscarPorNombre(String nombreCategoria){
        return categoriaRepository.findByNombreCategoria(nombreCategoria)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se ha encontrado la Categoria"));
    }

    public List<Categoria> listarTodo(){
        return categoriaRepository.findAll();
    }
}


