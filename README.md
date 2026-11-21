# GeekManga Backend

API REST de la tienda **Geek&Manga** hecha con Spring Boot 4, Spring Security con JWT, JPA y MySQL.

## Cómo levantarlo

1. Tener MySQL corriendo en `localhost:3306`. La base `GeekManga` se crea sola.
2. Revisar el usuario y la contraseña de MySQL en `backend/src/main/resources/application.properties`,
   o definir las variables de entorno `DB_USERNAME` y `DB_PASSWORD`.
3. Ejecutar desde la carpeta `backend`:
   ```bash
   ./mvnw spring-boot:run        # Windows: mvnw.cmd spring-boot:run
   ```
4. La API queda en `http://localhost:8081`.

## Swagger

`http://localhost:8081/swagger-ui.html`

Para probar endpoints protegidos:
1. Ejecutar `POST /api/v1/auth/login` con un usuario de prueba.
2. Copiar el valor de `token`.
3. Presionar **Authorize** y pegar el token (sin escribir "Bearer").

## Usuarios de prueba (se crean al iniciar)

| Rol      | Correo              | Contraseña   |
|----------|---------------------|--------------|
| ADMIN    | admin@gmail.com     | admin123     |
| VENDEDOR | vendedor@gmail.com  | vendedor123  |
| USER     | cliente@gmail.com   | cliente123   |

## Roles y permisos

| Recurso | Público | USER (cliente) | VENDEDOR | ADMIN |
|---|---|---|---|---|
| Ver productos y categorías | ✔ | ✔ | ✔ | ✔ |
| Crear / editar / eliminar productos y categorías | | | | ✔ |
| Comprar (crear boleta) y ver mis boletas | | ✔ | ✔ | ✔ |
| Ver todas las boletas | | | ✔ | ✔ |
| Administrar usuarios | | | | ✔ |
| Ver / editar su propia cuenta | | ✔ | ✔ | ✔ |

## Autenticación (JWT)

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/v1/auth/login` | `{ correo, password }` → `{ token, refreshToken, expiraEnMs, usuario }` |
| POST | `/api/v1/auth/registro` | Crea un cliente (rol USER) y devuelve sus tokens |
| POST | `/api/v1/auth/refresh` | `{ refreshToken }` → tokens nuevos |
| GET  | `/api/v1/auth/me` | Datos del usuario del token |

- El **access token** dura 1 hora y se envía en cada petición: `Authorization: Bearer <token>`.
- El **refresh token** dura 7 días y solo sirve para pedir un access token nuevo.
- Las contraseñas se guardan encriptadas con **BCrypt** y nunca se devuelven en las respuestas.

## Endpoints principales

| Recurso | Rutas |
|---|---|
| Productos | `GET /api/v1/producto`, `GET /ofertas`, `GET /categoria/{id}`, `GET /buscar/{id}`, `GET /buscar/nombre/{nombre}`, `POST`, `PUT /actualizar/{id}`, `DELETE /eliminar/{id}` |
| Categorías | `GET /api/v1/categoria`, `GET /buscar/{id}`, `GET /buscar/nombre/{nombre}`, `POST`, `PUT /actualizar/{id}`, `DELETE /eliminar/{id}` |
| Usuarios | `GET /api/v1/usuario`, `GET /buscar/{id}`, `POST`, `PUT /actualizar/{id}`, `DELETE /eliminar/{id}` |
| Boletas | `POST /api/v1/boleta`, `GET /api/v1/boleta`, `GET /mis-boletas`, `GET /buscar/{id}` |

Los errores siempre responden con el mismo formato:
```json
{ "timestamp": "...", "status": 409, "error": "Stock insuficiente", "mensaje": "No hay suficiente stock para ..." }
```

## Tests

```bash
./mvnw test
```
Las pruebas de integración (`ApiIntegracionTests`) usan una base H2 en memoria, así que no necesitan MySQL.
