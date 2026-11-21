package GeekManga.backend.detalle;

import GeekManga.backend.boleta.Boleta;
import GeekManga.backend.producto.Producto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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
    private Producto producto;
}