package br.unifametro.service;

import br.unifametro.exception.RecursoNaoEncontradoException;
import br.unifametro.model.Cliente;
import br.unifametro.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    public List<Cliente> listarTodos() {
        return repository.findAll();
    }

    public Cliente buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Cliente não encontrado"
                        ));
    }

    public Cliente salvar(Cliente cliente) {

        if (repository.existsByEmail(cliente.getEmail())) {
            throw new IllegalArgumentException(
                    "E-mail já cadastrado"
            );
        }

        return repository.save(cliente);
    }

    public Cliente atualizar(Long id, Cliente dados) {

        Cliente cliente = buscarPorId(id);

        cliente.setNome(dados.getNome());
        cliente.setEmail(dados.getEmail());
        cliente.setCpf(dados.getCpf());
        cliente.setTelefone(dados.getTelefone());

        return repository.save(cliente);
    }

    public void excluir(Long id) {

        Cliente cliente = buscarPorId(id);

        repository.delete(cliente);
    }
}