package br.unifametro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.unifametro.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    boolean existsByEmail(String email);
}