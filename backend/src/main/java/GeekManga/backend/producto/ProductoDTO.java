package GeekManga.backend.producto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Schema(description = "Entidad que representa a un producto registrado en el sistema")
public class ProductoDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Identificador único del producto",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    private String codigo;

    private String producto;

    private int precio;

    private String marca;

    private int stock;

    private int stockCritico;

    private String categoria;

    private boolean oferta;

    private int precioOferta;

    private String descripcion;

    private String urlFoto;
}
