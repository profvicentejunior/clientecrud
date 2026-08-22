package br.unifametro.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import br.unifametro.model.Cliente;
import br.unifametro.repository.ClienteRepository;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;

    @BeforeEach
    void prepararDadosDoTeste() {

        cliente = new Cliente();

        cliente.setNome("Maria da Silva");
        cliente.setEmail("maria@email.com");
        cliente.setCpf("111.222.333-44");
        cliente.setTelefone("(85) 99999-1111");
    }

    @Test
    void deveSalvarClienteQuandoEmailNaoEstiverCadastrado() {

        when(clienteRepository.existsByEmail(cliente.getEmail()))
                .thenReturn(false);

        when(clienteRepository.save(cliente))
                .thenReturn(cliente);

        Cliente resultado =
                clienteService.salvar(cliente);

        assertNotNull(resultado);

        assertEquals(
                "Maria da Silva",
                resultado.getNome()
        );

        verify(clienteRepository)
                .existsByEmail(cliente.getEmail());

        verify(clienteRepository)
                .save(cliente);
    }

    @Test
    void naoDeveSalvarClienteQuandoEmailJaEstiverCadastrado() {

        when(clienteRepository.existsByEmail(cliente.getEmail()))
                .thenReturn(true);

        IllegalArgumentException excecao =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> clienteService.salvar(cliente)
                );

        assertEquals(
                "E-mail já cadastrado",
                excecao.getMessage()
        );

        verify(clienteRepository, never())
                .save(any());
    }

    @Test
    void deveBuscarClienteQuandoIdExistir() {

        cliente.setId(1L);

        when(clienteRepository.findById(1L))
                .thenReturn(Optional.of(cliente));

        Cliente resultado =
                clienteService.buscarPorId(1L);

        assertNotNull(resultado);

        assertEquals(
                1L,
                resultado.getId()
        );
    }

    @Test
    void deveLancarErroQuandoClienteNaoExistir() {

        when(clienteRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> clienteService.buscarPorId(999L)
        );
    }
}