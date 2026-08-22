package br.unifametro.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.unifametro.model.Cliente;
import br.unifametro.repository.ClienteRepository;

@SpringBootTest
@AutoConfigureMockMvc
class ClienteIntegracaoTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClienteRepository clienteRepository;

    @BeforeEach
    void limparBancoAntesDeCadaTeste() {

        clienteRepository.deleteAll();
    }

    @Test
    void deveCadastrarClienteEConsultarNoBancoComSucesso()
            throws Exception {

        Cliente cliente = criarClienteValido();

        mockMvc.perform(
                post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cliente))
        )
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.nome")
                .value("Carlos Oliveira"));

        mockMvc.perform(
                get("/api/clientes")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].nome")
                .value("Carlos Oliveira"));
    }

    @Test
    void naoDeveCadastrarDoisClientesComMesmoEmail()
            throws Exception {

        Cliente primeiroCliente =
                criarClienteValido();

        clienteRepository.save(primeiroCliente);

        Cliente clienteDuplicado =
                criarClienteValido();

        mockMvc.perform(
                post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(
                                        clienteDuplicado
                                )
                        )
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.mensagem")
                .value("E-mail já cadastrado"));
    }

    private Cliente criarClienteValido() {

        Cliente cliente = new Cliente();

        cliente.setNome("Carlos Oliveira");
        cliente.setEmail("carlos@email.com");
        cliente.setCpf("555.666.777-88");
        cliente.setTelefone("(85) 98888-7777");

        return cliente;
    }
}