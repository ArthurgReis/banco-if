package br.com.bancofeira.banco_feira.repository;

import br.com.bancofeira.banco_feira.model.Empresa;
import br.com.bancofeira.banco_feira.model.Evento;
import br.com.bancofeira.banco_feira.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Integer> {
    
    boolean existsByEventoAndFuncionariosContaining(Evento evento, Usuario funcionario);
    List<Empresa> findByEventoId(Integer eventoId);
}
