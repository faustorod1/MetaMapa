package ar.utn.ba.ddsi;

import ar.utn.ba.ddsi.models.repositories.IColeccionesRepository;
import ar.utn.ba.ddsi.models.repositories.IHechosRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest(classes = AgregadorApplication.class)
@AutoConfigureMockMvc
public class ColeccionesTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IColeccionesRepository coleccionesRepository;
    @MockBean
    private IHechosRepository hechosRepository;

    @Test
    public void testApiColeccionIsOk() throws Exception {
        mockMvc.perform(get("/api/colecciones")) // Hace una petición mockeada a la API
                .andExpect(status().isOk()); // Controla que el código de estado sea ~200

    }

    @Test
    public void testApiColeccionDevuelveColeccionesCoherentes() throws Exception {
        when(coleccionesRepository.findAll())
                .thenReturn(FakeRepository.colecciones());

        mockMvc.perform(get("/api/colecciones"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json")) // Controla que haya devuelto un json
                .andExpect(jsonPath("$").isArray()) // Verifica que el contenido del json sea un array
                .andExpect(jsonPath("$[0]").exists()) // Verifica que tenga al menos un hecho
                .andExpect(jsonPath("$[0].titulo").exists()) // Comprueba que el hecho tenga título
                .andExpect(jsonPath("$[0].titulo").isNotEmpty()); // Comprueba que el título tenga contenido
    }

    @Test
    public void testObtenerHechosDeUnaColeccionIsOk() throws Exception {
        String idColeccion = "1";

        when(coleccionesRepository.findByIdentificador(idColeccion))
                .thenReturn(FakeRepository.coleccionPorId(idColeccion));

        mockMvc.perform(get(String.format("/api/colecciones/%s/hechos", idColeccion)))
                .andExpect(status().isOk());

    }

    @Test
    public void testObtenerHechosDeUnaColeccionDevuelveLosHechosDeLaColeccion() throws Exception {
        String idColeccion = "1";

        when(coleccionesRepository.findByIdentificador(idColeccion))
                .thenReturn(FakeRepository.coleccionPorId(idColeccion));
        when(hechosRepository.findAll())
                .thenReturn(FakeRepository.hechos());

        mockMvc.perform(get(String.format("/api/colecciones/%s/hechos", idColeccion)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json")) // Controla que haya devuelto un json
                .andExpect(jsonPath("$").isArray()) // Verifica que el contenido del json sea un array
                .andExpect(jsonPath("$.length()").value(2)) // Verifica que esté la cantidad correcta de hechos
                .andExpect(jsonPath("$[0].categoria").value("Caída de aeronave")) // Verifica que cumplan el criterio de la colección
                .andExpect(jsonPath("$[1].categoria").value("Caída de aeronave"));

    }
}
