package GeekManga.backend.error;

/** Se lanza cuando la operación choca con datos existentes (correo repetido, categoría con productos, etc.). */
public class ConflictoException extends RuntimeException {
    public ConflictoException(String message) {
        super(message);
    }
}
