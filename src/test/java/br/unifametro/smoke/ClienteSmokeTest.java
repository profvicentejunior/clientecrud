package br.unifametro.smoke;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ClienteSmokeTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveSubirContextoDaAplicacaoComSucesso() {

        // Se o Spring não conseguir subir,
        // este teste falhará antes mesmo de terminar.

    }

    @Test
    void deveResponderEndpointPrincipalDeClientes()
            throws Exception {

        mockMvc.perform(
                get("/api/clientes")
        )
        .andExpect(status().isOk());
    }

    @Test
    void deveRetornarErroAoConsultarClienteInexistente()
            throws Exception {

        mockMvc.perform(
                get("/api/clientes/999999")
        )
        .andExpect(status().isNotFound());
    }
}