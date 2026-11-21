package GeekManga.backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * Documentación Swagger: http://localhost:8081/swagger-ui.html
 *
 * Para probar endpoints protegidos: hacer login en /api/v1/auth/login, copiar el "token"
 * y pegarlo en el botón "Authorize" (sin escribir "Bearer").
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "GeekManga API",
                version = "1.0",
                description = "API REST de la tienda Geek&Manga: productos, categorías, usuarios, boletas y autenticación JWT"
        ),
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {
}
