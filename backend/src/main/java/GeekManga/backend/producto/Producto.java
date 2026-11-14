package GeekManga.backend.producto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "producto")
@Schema(description = "Entidad que representa a un producto registrado en el sistema")
@Entity
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Identificador único del producto para gestón interno del sistema técnico",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED, accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Código comercial único (SKU) para el negocio, inventario y punto de venta",
            example = "M0R-BAV-001",
            requiredMode = Schema.RequiredMode.REQUIRED, accessMode = Schema.AccessMode.READ_WRITE)
    private String codigo;

    @Size(min = 2, max = 50)
    @NotBlank
    @Schema(description = "Nombre del producto",
            example = "Caja Watts",
            requiredMode = Schema.RequiredMode.REQUIRED, accessMode = Schema.AccessMode.READ_WRITE)
    private String producto;

    @NotNull
    @Schema(description = "Precio del producto",
            example = "10000",
            requiredMode = Schema.RequiredMode.REQUIRED, accessMode = Schema.AccessMode.READ_WRITE)
    private int precio;

    @NotBlank
    @Schema(description = "Marca, corporación del producto",
            example = "Soprole",
            requiredMode = Schema.RequiredMode.REQUIRED, accessMode = Schema.AccessMode.READ_WRITE)
    private String marca;

    @NotNull
    @Schema(description = "Unidades disponibles para venta del producto",
            example = "14",
            requiredMode = Schema.RequiredMode.REQUIRED, accessMode = Schema.AccessMode.READ_WRITE)
    private int stock;

    @NotNull
    @Schema(description = "Límite en el que el sistema considera que hay muy poca cantidad disponible de un producto",
            example = "5",
            requiredMode = Schema.RequiredMode.REQUIRED, accessMode = Schema.AccessMode.READ_WRITE)
    private int stockCritico;

    @NotBlank
    @Schema(description = "Categoria, tipo, al que pertenece el producto",
            example = "Tecnología",
            requiredMode = Schema.RequiredMode.REQUIRED, accessMode = Schema.AccessMode.READ_WRITE)
    private String categoria;


    @Schema(description = "Señala al sistema si un producto debe llevar su precio de oferta o no",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED, accessMode = Schema.AccessMode.READ_WRITE)
    private boolean oferta;



    @Schema(description = "Precio perteneciente únicamente a cuando un producto tiene su oferta activa",
            example = "3900",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED, accessMode = Schema.AccessMode.READ_WRITE)
    private int precioOferta;


    @Size(min = 1, max = 300)
    @Schema(description = "Descripción del producto",
            example = "Caja tetrapack de 200ml de jugo de durazno de la marca Nestlé",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED, accessMode = Schema.AccessMode.READ_WRITE)
    private String descripcion;

    @Schema(description = "Ubicación de una imagen para mostrar junto a un producto, generada automáticamente al subir una imagen",
            example = "carpeta/carpeta/archivo.jpg",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED, accessMode = Schema.AccessMode.READ_WRITE)
    private String urlFoto;
}
