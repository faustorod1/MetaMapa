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
public class FuenteEstaticaTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testApiHechos() throws Exception {
        mockMvc.perform(get("/hechos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").isArray());
    }
}
