package GeekManga.backend.producto;

import GeekManga.backend.categoria.Categoria;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "productos")
@Schema(description = "Entidad que representa a un producto registrado en el sistema")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Identificador único del producto para gestión interna del sistema",
            example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    @NotBlank(message = "El código es obligatorio")
    @Size(max = 50)
    @Schema(description = "Código comercial único (SKU) del producto", example = "M001")
    private String codigo;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(min = 2, max = 50)
    @Schema(description = "Nombre del producto", example = "One Piece - Tomo 1")
    private String producto;

    // Se usan Integer/Boolean (no int/boolean): con Jackson 3 un campo primitivo que falta en el JSON
    // hace fallar la lectura completa del cuerpo; con wrappers llega null y @NotNull indica qué campo falta.
    @Column(nullable = false)
    @NotNull(message = "El precio es obligatorio")
    @Min(value = 0, message = "El precio no puede ser negativo")
    @Schema(description = "Precio normal del producto", example = "15990")
    private Integer precio;

    @Column(nullable = false)
    @NotBlank(message = "La marca es obligatoria")
    @Schema(description = "Marca o editorial del producto", example = "Shueisha")
    private String marca;

    @Column(nullable = false)
    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Schema(description = "Unidades disponibles para la venta", example = "14")
    private Integer stock;

    @Column(nullable = false)
    @NotNull(message = "El stock crítico es obligatorio")
    @Min(value = 0, message = "El stock crítico no puede ser negativo")
    @Schema(description = "Cantidad bajo la cual se considera que queda poco stock", example = "5")
    private Integer stockCritico;

    @Column(nullable = false)
    @Schema(description = "Indica si el producto se vende con su precio de oferta (si no se envía, queda en false)", example = "true")
    private Boolean oferta = false;

    @Min(value = 0, message = "El precio de oferta no puede ser negativo")
    @Schema(description = "Precio cuando la oferta está activa (debe ser menor al precio normal)",
            example = "12990", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer precioOferta;

    @Size(max = 300)
    @Schema(description = "Descripción del producto", example = "Primera edición del clásico manga de Eiichiro Oda.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String descripcion;

    @Schema(description = "Ruta de la imagen del producto", example = "/images/onepiece.jpg",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String urlFoto;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    @NotNull(message = "La categoría es obligatoria")
    @Schema(description = "Categoría del producto. Al crear o editar basta con enviar { \"categoriaId\": 1 }")
    private Categoria categoria;

    /** Nunca devuelve null: si no se indicó, el producto no está en oferta. */
    public Boolean getOferta() {
        return Boolean.TRUE.equals(oferta);
    }
}
