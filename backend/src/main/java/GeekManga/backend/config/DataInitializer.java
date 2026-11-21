package GeekManga.backend.config;

import GeekManga.backend.categoria.Categoria;
import GeekManga.backend.categoria.CategoriaRepository;
import GeekManga.backend.producto.Producto;
import GeekManga.backend.producto.ProductoRepository;
import GeekManga.backend.usuario.Rol;
import GeekManga.backend.usuario.Usuario;
import GeekManga.backend.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Carga datos de ejemplo al iniciar la aplicación (solo lo que todavía no exista):
 * - 3 usuarios, uno por rol (las contraseñas se guardan encriptadas con BCrypt).
 * - Categorías y productos de la tienda.
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        crearUsuario("Administrador", "GeekManga", "11111111-1", "admin@gmail.com", "admin123", Rol.ADMIN);
        crearUsuario("Vendedor", "GeekManga", "22222222-2", "vendedor@gmail.com", "vendedor123", Rol.VENDEDOR);
        crearUsuario("Cliente", "Demo", "33333333-3", "cliente@gmail.com", "cliente123", Rol.USER);

        Categoria manga = crearCategoria("Manga");
        Categoria figura = crearCategoria("Figura");
        Categoria comic = crearCategoria("Comic");

        crearProducto("M001", "One Piece - Tomo 1", "Shueisha", 15990, 12990, true, 12, 5,
                "Primera edición del clásico manga de Eiichiro Oda.", "/images/onepiece.jpg", manga);
        crearProducto("F001", "Figura Naruto", "Banpresto", 24990, null, false, 8, 3,
                "Figura coleccionable del personaje Naruto Uzumaki.", "/images/naruto.jpg", figura);
        crearProducto("C001", "Comic Marvel - Spider-Man", "Marvel", 12990, 9990, true, 10, 4,
                "Edición especial de Spider-Man para coleccionistas.", "/images/spiderman.jpg", comic);
        crearProducto("M002", "Berserk - Tomo 1", "Panini", 18990, null, false, 3, 5,
                "Obra maestra de Kentaro Miura.", "/images/berserk.jpg", manga);
    }

    private void crearUsuario(String nombre, String apellidos, String run, String correo, String password, Rol rol) {
        if (usuarioRepository.existsByCorreo(correo) || usuarioRepository.existsByRun(run)) {
            return;
        }
        Usuario u = new Usuario();
        u.setNombre(nombre);
        u.setApellidos(apellidos);
        u.setRun(run);
        u.setCorreo(correo);
        u.setPassword(passwordEncoder.encode(password));
        u.setTipoUsuario(rol);
        usuarioRepository.save(u);
        log.info("Usuario de prueba creado: {} ({})", correo, rol);
    }

    private Categoria crearCategoria(String nombre) {
        return categoriaRepository.findByNombreCategoriaIgnoreCase(nombre).orElseGet(() -> {
            Categoria c = new Categoria();
            c.setNombreCategoria(nombre);
            return categoriaRepository.save(c);
        });
    }

    private void crearProducto(String codigo, String nombre, String marca, int precio, Integer precioOferta,
                               boolean oferta, int stock, int stockCritico, String descripcion,
                               String urlFoto, Categoria categoria) {
        if (productoRepository.findByCodigo(codigo).isPresent()) {
            return;
        }
        Producto p = new Producto();
        p.setCodigo(codigo);
        p.setProducto(nombre);
        p.setMarca(marca);
        p.setPrecio(precio);
        p.setPrecioOferta(precioOferta);
        p.setOferta(oferta);
        p.setStock(stock);
        p.setStockCritico(stockCritico);
        p.setDescripcion(descripcion);
        p.setUrlFoto(urlFoto);
        p.setCategoria(categoria);
        productoRepository.save(p);
    }
}
