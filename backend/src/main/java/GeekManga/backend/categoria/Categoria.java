package GeekManga.backend.categoria;

import GeekManga.backend.producto.Producto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categorias")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoriaId;

    private String nombreCategoria;

    @OneToMany(mappedBy = "categoria")
    @JsonIgnore
    private List<Producto> productos = new ArrayList<>();
}
