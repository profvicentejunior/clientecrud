package br.unifametro.controller;

import br.unifametro.model.Cliente;
import br.unifametro.service.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClienteService clienteService;

    @Test
    void deveCadastrarClienteQuandoDadosForemValidos() throws Exception {

        Cliente cliente = criarClienteValido();

        Cliente clienteSalvo = criarClienteValido();
        clienteSalvo.setId(1L);

        when(clienteService.salvar(any(Cliente.class)))
                .thenReturn(clienteSalvo);

        mockMvc.perform(
                post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cliente))
        )
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.nome").value("João da Silva"))
        .andExpect(jsonPath("$.email").value("joao@email.com"));
    }

    @Test
    void deveRetornarErroQuandoNomeForVazio() throws Exception {

        Cliente cliente = criarClienteValido();

        cliente.setNome("");

        mockMvc.perform(
                post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cliente))
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.mensagem").value("Dados inválidos"))
        .andExpect(jsonPath("$.erros.nome").exists());
    }

    private Cliente criarClienteValido() {

        Cliente cliente = new Cliente();

        cliente.setNome("João da Silva");
        cliente.setEmail("joao@email.com");
        cliente.setCpf("123.456.789-00");
        cliente.setTelefone("(85) 99999-9999");

        return cliente;
    }
}