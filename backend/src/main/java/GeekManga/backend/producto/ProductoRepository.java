package GeekManga.backend.producto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    Optional<Producto> findByProductoIgnoreCase(String producto);

    Optional<Producto> findByCodigo(String codigo);

    List<Producto> findByOfertaTrue();

    List<Producto> findByCategoria_CategoriaId(Long categoriaId);

    boolean existsByCategoria_CategoriaId(Long categoriaId);
}
