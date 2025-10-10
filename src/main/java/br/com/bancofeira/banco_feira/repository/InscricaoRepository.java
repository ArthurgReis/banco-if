package br.com.bancofeira.banco_feira.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.bancofeira.banco_feira.model.Evento;
import br.com.bancofeira.banco_feira.model.Inscricao;
import br.com.bancofeira.banco_feira.model.Usuario;

public interface InscricaoRepository extends JpaRepository<Inscricao, Integer> {

    boolean existsByUsuarioAndEvento(Usuario usuario, Evento evento);
}