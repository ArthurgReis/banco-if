package br.com.bancofeira.banco_feira.repository;

import br.com.bancofeira.banco_feira.model.Empresa;
import br.com.bancofeira.banco_feira.model.Evento;
import br.com.bancofeira.banco_feira.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Integer> {
    
    boolean existsByEventoAndFuncionariosContaining(Evento evento, Usuario funcionario);
    List<Empresa> findByEventoId(Integer eventoId);
    List<Empresa> findByEventoIdAndFuncionariosContains(Integer eventoId, Usuario funcionario);
    Optional<Empresa> findByChaveFuncionario(String chaveFuncionario);

    @Query("SELECT e FROM Empresa e LEFT JOIN FETCH e.funcionarios f WHERE e.evento.id = :eventoId ORDER BY e.creditos DESC")
    List<Empresa> findRankingByEventoId(@Param("eventoId") Integer eventoId);

    @Query("SELECT e FROM Empresa e LEFT JOIN FETCH e.funcionarios f WHERE e.id = :id")
    Optional<Empresa> findByIdWithFuncionarios(@Param("id") Integer id);
}
