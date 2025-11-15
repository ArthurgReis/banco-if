package br.com.bancofeira.banco_feira.repository;

import br.com.bancofeira.banco_feira.model.Empresa;
import br.com.bancofeira.banco_feira.model.Evento;
import br.com.bancofeira.banco_feira.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Integer> {
    
    boolean existsByEventoAndFuncionariosContaining(Evento evento, Usuario funcionario);
    List<Empresa> findByEventoId(Integer eventoId);
    List<Empresa> findByEventoIdAndFuncionariosContains(Integer eventoId, Usuario funcionario);
    Optional<Empresa> findByChaveFuncionario(String chaveFuncionario);
}
