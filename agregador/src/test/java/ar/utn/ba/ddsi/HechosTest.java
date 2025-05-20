package ar.utn.ba.ddsi;

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
public class HechosTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IHechosRepository hechosRepository;

    @Test
    public void testApiHechosIsOk() throws Exception {
        mockMvc.perform(get("/api/hechos")) // Hace una petición mockeada a la API
                .andExpect(status().isOk()); // Controla que el código de estado sea ~200

    }

    @Test
    public void testApiHechosDevuelveHechosCoherentes() throws Exception {
        when(hechosRepository.findAll())
                .thenReturn(FakeRepository.hechos());

        mockMvc.perform(get("/api/hechos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json")) // Controla que haya devuelto un json
                .andExpect(jsonPath("$").isArray()) // Verifica que el contenido del json sea un array
                .andExpect(jsonPath("$[0]").exists()) // Verifica que tenga al menos un hecho
                .andExpect(jsonPath("$[0].titulo").exists()) // Comprueba que el hecho tenga título
                .andExpect(jsonPath("$[0].titulo").isNotEmpty()); // Comprueba que el título tenga contenido
    }
}
