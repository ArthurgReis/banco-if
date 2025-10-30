package br.com.bancofeira.banco_feira.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.bancofeira.banco_feira.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{
    
    Optional<Usuario> findByCpf(String cpf);

    Optional<Usuario> findByEmail(String email);
    
}
