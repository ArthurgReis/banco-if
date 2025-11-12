package br.com.bancofeira.banco_feira.repository;

import br.com.bancofeira.banco_feira.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{
    
    Optional<Usuario> findByCpf(String cpf);

    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.roles LEFT JOIN FETCH u.empresas WHERE u.email = :email")
    Optional<Usuario> findByEmailWithRolesAndEmpresas(@Param("email") String email);

    Optional<Usuario> findByEmail(String email);
    
}
