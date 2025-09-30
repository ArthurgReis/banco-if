package br.com.bancofeira.banco_feira.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.bancofeira.banco_feira.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByNome(String nome);
}