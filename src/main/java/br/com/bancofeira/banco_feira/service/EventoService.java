package br.com.bancofeira.banco_feira.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.bancofeira.banco_feira.model.Evento;
import br.com.bancofeira.banco_feira.repository.EventoRepository;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;

    public EventoService(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    public Evento criarEvento(Evento evento) {
        String chave = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        evento.setChaveInscricao(chave);
        return eventoRepository.save(evento);
    }
}