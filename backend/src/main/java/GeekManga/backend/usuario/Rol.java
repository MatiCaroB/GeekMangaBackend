package GeekManga.backend.usuario;

/**
 * Roles del sistema. Se guardan como texto en la columna tipo_usuario.
 * Spring Security los usa con el prefijo "ROLE_" (ROLE_ADMIN, ROLE_VENDEDOR, ROLE_USER).
 *
 * ADMIN    -> administra productos, categorías, usuarios y ve todas las boletas.
 * VENDEDOR -> ve productos y todas las boletas (no edita catálogo ni usuarios).
 * USER     -> cliente de la tienda: compra y ve solo sus propias boletas.
 */
public enum Rol {
    ADMIN,
    VENDEDOR,
    USER
}
