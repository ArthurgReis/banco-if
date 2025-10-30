package br.com.bancofeira.banco_feira.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.bancofeira.banco_feira.model.Evento;

public interface EventoRepository extends JpaRepository<Evento, Integer> {
    Optional<Evento> findByChaveInscricao(String chave);
}