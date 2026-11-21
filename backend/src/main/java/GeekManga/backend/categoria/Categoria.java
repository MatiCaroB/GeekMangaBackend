package GeekManga.backend.categoria;

import GeekManga.backend.producto.Producto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

// @Getter/@Setter en vez de @Data: @Data genera toString/hashCode que recorren la relación
// Categoria <-> Producto y pueden terminar en StackOverflowError.
@Entity
@Table(name = "categorias")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Categoría a la que pertenecen los productos")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador de la categoría", example = "1")
    private Long categoriaId;

    @Column(nullable = false, unique = true, length = 50)
    @NotBlank(message = "El nombre de la categoría es obligatorio")
    @Size(max = 50)
    @Schema(description = "Nombre de la categoría", example = "Manga")
    private String nombreCategoria;

    @OneToMany(mappedBy = "categoria")
    @JsonIgnore
    private List<Producto> productos = new ArrayList<>();
}
