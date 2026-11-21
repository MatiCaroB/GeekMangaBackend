package GeekManga.backend;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas de integración: levantan la aplicación completa (con seguridad y base H2)
 * y hacen peticiones HTTP reales a los endpoints, igual que lo haría el frontend.
 */
@SpringBootTest
class ApiIntegracionTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    private String login(String correo, String password) throws Exception {
        String json = mvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"correo\":\"" + correo + "\",\"password\":\"" + password + "\"}"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return JsonPath.read(json, "$.token");
    }

    private String bearer(String token) {
        return "Bearer " + token;
    }

    // ---------- Autenticación ----------

    @Test
    void loginCorrectoDevuelveTokensYRol() throws Exception {
        mvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"correo\":\"admin@gmail.com\",\"password\":\"admin123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", not(emptyString())))
                .andExpect(jsonPath("$.refreshToken", not(emptyString())))
                .andExpect(jsonPath("$.usuario.rol", is("ADMIN")));
    }

    @Test
    void loginConClaveIncorrectaDevuelve401() throws Exception {
        mvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"correo\":\"admin@gmail.com\",\"password\":\"mala\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void registroCreaUsuarioConRolUser() throws Exception {
        mvc.perform(post("/api/v1/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombre":"Ana","apellidos":"Pérez","run":"44444444-4",
                                 "correo":"Ana@Gmail.com","password":"ana1234"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.usuario.rol", is("USER")))
                .andExpect(jsonPath("$.usuario.correo", is("ana@gmail.com")));

        // el correo repetido se rechaza
        mvc.perform(post("/api/v1/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombre":"Ana","run":"55555555-5","correo":"ana@gmail.com","password":"ana1234"}
                                """))
                .andExpect(status().isConflict());
    }

    @Test
    void refreshEntregaTokenNuevoYAccessTokenNoSirveComoRefresh() throws Exception {
        String json = mvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"correo\":\"cliente@gmail.com\",\"password\":\"cliente123\"}"))
                .andReturn().getResponse().getContentAsString();
        String access = JsonPath.read(json, "$.token");
        String refresh = JsonPath.read(json, "$.refreshToken");

        mvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"refreshToken\":\"" + refresh + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", not(emptyString())));

        mvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"refreshToken\":\"" + access + "\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void meDevuelveUsuarioDelToken() throws Exception {
        String token = login("vendedor@gmail.com", "vendedor123");
        mvc.perform(get("/api/v1/auth/me").header(HttpHeaders.AUTHORIZATION, bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rol", is("VENDEDOR")));

        mvc.perform(get("/api/v1/auth/me")).andExpect(status().isUnauthorized());
        mvc.perform(get("/api/v1/auth/me").header(HttpHeaders.AUTHORIZATION, "Bearer token-falso"))
                .andExpect(status().isUnauthorized());
    }

    // ---------- Productos y roles ----------

    @Test
    void catalogoEsPublico() throws Exception {
        mvc.perform(get("/api/v1/producto"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(4))));
        mvc.perform(get("/api/v1/categoria")).andExpect(status().isOk());
        mvc.perform(get("/api/v1/producto/ofertas")).andExpect(status().isOk());
    }

    @Test
    void crearProductoSegunRol() throws Exception {
        String producto = """
                {"codigo":"T001","producto":"Producto Test","marca":"Test","precio":5000,
                 "stock":5,"stockCritico":1,"oferta":false,"categoria":{"categoriaId":1}}
                """;

        // sin token -> 401
        mvc.perform(post("/api/v1/producto").contentType(MediaType.APPLICATION_JSON).content(producto))
                .andExpect(status().isUnauthorized());

        // cliente -> 403
        String cliente = login("cliente@gmail.com", "cliente123");
        mvc.perform(post("/api/v1/producto").header(HttpHeaders.AUTHORIZATION, bearer(cliente))
                        .contentType(MediaType.APPLICATION_JSON).content(producto))
                .andExpect(status().isForbidden());

        // vendedor -> 403
        String vendedor = login("vendedor@gmail.com", "vendedor123");
        mvc.perform(post("/api/v1/producto").header(HttpHeaders.AUTHORIZATION, bearer(vendedor))
                        .contentType(MediaType.APPLICATION_JSON).content(producto))
                .andExpect(status().isForbidden());

        // admin -> 201
        String admin = login("admin@gmail.com", "admin123");
        mvc.perform(post("/api/v1/producto").header(HttpHeaders.AUTHORIZATION, bearer(admin))
                        .contentType(MediaType.APPLICATION_JSON).content(producto))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.categoria.nombreCategoria", is("Manga")));
    }

    @Test
    void productoInvalidoDevuelve400ConCampos() throws Exception {
        String admin = login("admin@gmail.com", "admin123");
        mvc.perform(post("/api/v1/producto").header(HttpHeaders.AUTHORIZATION, bearer(admin))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"codigo\":\"\",\"producto\":\"\",\"precio\":-1}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.producto").exists())
                .andExpect(jsonPath("$.campos.precio").exists())
                .andExpect(jsonPath("$.campos.stock").exists())       // faltaba en el JSON
                .andExpect(jsonPath("$.campos.categoria").exists());
    }

    // ---------- Usuarios ----------

    @Test
    void listarUsuariosSoloAdminYSinPasswords() throws Exception {
        String admin = login("admin@gmail.com", "admin123");
        mvc.perform(get("/api/v1/usuario").header(HttpHeaders.AUTHORIZATION, bearer(admin)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].password").doesNotExist());

        String cliente = login("cliente@gmail.com", "cliente123");
        mvc.perform(get("/api/v1/usuario").header(HttpHeaders.AUTHORIZATION, bearer(cliente)))
                .andExpect(status().isForbidden());
    }

    // ---------- Boletas ----------

    @Test
    void comprarDescuentaStockYRechazaSinStock() throws Exception {
        String cliente = login("cliente@gmail.com", "cliente123");

        // Producto 1 (One Piece) está en oferta: 12990 c/u
        mvc.perform(post("/api/v1/boleta").header(HttpHeaders.AUTHORIZATION, bearer(cliente))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"items\":[{\"productoId\":1,\"cantidad\":2}]}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.total", is(25980)))
                .andExpect(jsonPath("$.usuario.password").doesNotExist());

        mvc.perform(post("/api/v1/boleta").header(HttpHeaders.AUTHORIZATION, bearer(cliente))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"items\":[{\"productoId\":1,\"cantidad\":9999}]}"))
                .andExpect(status().isConflict());

        mvc.perform(get("/api/v1/boleta/mis-boletas").header(HttpHeaders.AUTHORIZATION, bearer(cliente)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));

        // el cliente no ve todas las boletas; el vendedor sí
        mvc.perform(get("/api/v1/boleta").header(HttpHeaders.AUTHORIZATION, bearer(cliente)))
                .andExpect(status().isForbidden());
        String vendedor = login("vendedor@gmail.com", "vendedor123");
        mvc.perform(get("/api/v1/boleta").header(HttpHeaders.AUTHORIZATION, bearer(vendedor)))
                .andExpect(status().isOk());
    }

    @Test
    void swaggerEsAccesible() throws Exception {
        mvc.perform(get("/v3/api-docs")).andExpect(status().isOk());
    }
}
