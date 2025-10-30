package br.com.bancofeira.banco_feira.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.bancofeira.banco_feira.model.Empresa;
import br.com.bancofeira.banco_feira.model.Evento;
import br.com.bancofeira.banco_feira.model.Usuario;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Integer> {
    
    boolean existsByEventoAndFuncionariosContaining(Evento evento, Usuario funcionario);

}
