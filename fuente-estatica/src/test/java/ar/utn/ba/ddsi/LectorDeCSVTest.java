package ar.utn.ba.ddsi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = FuenteEstaticaApplication.class)
@AutoConfigureMockMvc
public class LectorDeCSVTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testApiHechosIsOk() throws Exception {
        mockMvc.perform(get("/api/hechos")) // Hace una petición mockeada a la API
                .andExpect(status().isOk()); // Controla que el código de estado sea ~200
    }

    @Test
    public void testApiHechosDevuelveHechosDelDataset() throws Exception {
        mockMvc.perform(get("/api/hechos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json")) // Controla que haya devuelto un json
                .andExpect(jsonPath("$").isArray()) // Verifica que el contenido del json sea un array
                .andExpect(jsonPath("$[0].titulo") // Verifica que el primer hecho devuelto sea el correcto
                        .value("Ráfagas de más de 100 km/h causa estragos en San Vicente, Misiones"));
    }
}
