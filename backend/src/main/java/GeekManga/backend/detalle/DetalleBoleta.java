package GeekManga.backend.detalle;

import GeekManga.backend.boleta.Boleta;
import GeekManga.backend.producto.Producto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Una línea de la boleta. Guarda el precio unitario del momento de la compra,
 * así la boleta no cambia si después se modifica el precio del producto.
 * Se guarda en cascada junto con la Boleta, por eso no necesita controller propio.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "detalle_boleta")
public class DetalleBoleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int cantidad;

    @Column(nullable = false)
    private int precioUnitario;

    @Column(nullable = false)
    private int subtotal;

    @ManyToOne(optional = false)
    @JoinColumn(name = "boleta_id")
    @JsonBackReference
    private Boleta boleta;

    @ManyToOne(optional = false)
    @JoinColumn(name = "producto_id")
    @JsonIgnoreProperties({"stock", "stockCritico", "descripcion", "categoria"})
    private Producto producto;
}
