package br.com.bancofeira.banco_feira.service;

import br.com.bancofeira.banco_feira.dto.EventoRequestDto;
import br.com.bancofeira.banco_feira.exception.ResourceNotFoundException;
import br.com.bancofeira.banco_feira.model.Evento;
import br.com.bancofeira.banco_feira.repository.EmpresaRepository;
import br.com.bancofeira.banco_feira.repository.EventoRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import br.com.bancofeira.banco_feira.model.Empresa;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;
    private final EmpresaRepository empresaRepository;

    public EventoService(EventoRepository eventoRepository, EmpresaRepository empresaRepository) {
        this.eventoRepository = eventoRepository;
        this.empresaRepository = empresaRepository;
    }

    public Evento criarEvento(EventoRequestDto eventoDto) {

        Evento novoEvento = new Evento();
        novoEvento.setNome(eventoDto.getNome());
        novoEvento.setDataRealizacao(eventoDto.getDataRealizacao());
        novoEvento.setCreditoInicialCliente(eventoDto.getCreditoInicialCliente());

        String chaveEmp = UUID.randomUUID().toString().substring(9, 17).toUpperCase();
        novoEvento.setChaveEmpresa(chaveEmp);

        return eventoRepository.save(novoEvento);
    }

    @SuppressWarnings("null")
    public List<Empresa> listarEmpresasPorEvento(Integer eventoId){
        if (!eventoRepository.existsById(eventoId)) {
            throw new ResourceNotFoundException("Evento não encontrado com o ID: " + eventoId);
        }
        
        return empresaRepository.findByEventoId(eventoId);
    }

    public List<Evento> listarEventos() {
        return eventoRepository.findAll();
    }

    @SuppressWarnings("null")
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

    @SuppressWarnings("null")
    @Transactional
    public void deletarEvento(Integer id) {
        Evento eventoParaDeletar = buscarEventoPorId(id);
        eventoRepository.delete(eventoParaDeletar);
    }
}