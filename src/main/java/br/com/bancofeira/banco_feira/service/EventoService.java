package br.com.bancofeira.banco_feira.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.bancofeira.banco_feira.exception.ResourceNotFoundException;
import br.com.bancofeira.banco_feira.model.Evento; 
import br.com.bancofeira.banco_feira.repository.EventoRepository;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;

    public EventoService(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    public Evento criarEvento(Evento evento) {

        String chaveCli = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String chaveEmp = UUID.randomUUID().toString().substring(9, 17).toUpperCase();
        evento.setChaveCliente(chaveCli);
        evento.setChaveEmpresa(chaveEmp);

        return eventoRepository.save(evento);
    }

    public List<Evento> listarEventos() {
        return eventoRepository.findAll();
    }

    public Evento buscarEventoPorId(Integer id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado com o ID: " + id));
    }

    @Transactional
    public Evento atualizarEvento(Integer id, Evento eventoAtualizado) {
        Evento eventoExistente = buscarEventoPorId(id);

        eventoExistente.setNome(eventoAtualizado.getNome());
        eventoExistente.setDataRealizacao(eventoAtualizado.getDataRealizacao());
        eventoExistente.setCreditoInicialCliente(eventoAtualizado.getCreditoInicialCliente());

        return eventoRepository.save(eventoExistente);
    }

    @Transactional
    public void deletarEvento(Integer id) {
        Evento eventoParaDeletar = buscarEventoPorId(id);
        eventoRepository.delete(eventoParaDeletar);
    }
}