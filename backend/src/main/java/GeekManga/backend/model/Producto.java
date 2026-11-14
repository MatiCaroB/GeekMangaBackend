package GeekManga.backend.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
