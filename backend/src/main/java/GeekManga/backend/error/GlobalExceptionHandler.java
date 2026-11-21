package GeekManga.backend.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(RecursoNoEncontradoException.class)
        public ResponseEntity handleRecursoNoEncontrado(RecursoNoEncontradoException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "timestamp", LocalDateTime.now().toString(),
                    "status", 404,
                    "error", "Recurso no encontrado",
                    "mensaje", ex.getMessage()
            ));
        }

        @ExceptionHandler(StockInsuficienteException.class)
        public ResponseEntity handleStockInsuficiente(StockInsuficienteException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "timestamp", LocalDateTime.now().toString(),
                    "status", 409,
                    "error", "Stock insuficiente",
                    "mensaje", ex.getMessage()
            ));
        }
    }

