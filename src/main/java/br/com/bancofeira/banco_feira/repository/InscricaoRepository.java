package br.com.bancofeira.banco_feira.repository;

import br.com.bancofeira.banco_feira.model.Evento;
import br.com.bancofeira.banco_feira.model.Inscricao;
import br.com.bancofeira.banco_feira.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InscricaoRepository extends JpaRepository<Inscricao, Integer> {

    boolean existsByUsuarioAndEvento(Usuario usuario, Evento evento);
    List<Inscricao> findByEventoId(Integer eventoId);
    Optional<Inscricao> findByUsuarioIdAndEventoId(Integer usuarioId, Integer eventoId);
    List<Inscricao> findByUsuario(Usuario usuario);
}