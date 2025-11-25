package br.com.bancofeira.banco_feira.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.bancofeira.banco_feira.model.Cliente;

public interface ClienteRepository extends JpaRepository <Cliente, Integer>{
    boolean existsByCpf(String cpf);
    Optional<Cliente> findByCpf(String cpf);
    
}
