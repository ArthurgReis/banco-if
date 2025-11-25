package br.com.bancofeira.banco_feira.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.bancofeira.banco_feira.exception.ResourceNotFoundException;
import br.com.bancofeira.banco_feira.model.Cliente;
import br.com.bancofeira.banco_feira.model.Evento;
import br.com.bancofeira.banco_feira.repository.ClienteRepository;
import br.com.bancofeira.banco_feira.repository.EventoRepository;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final EventoRepository eventoRepository;

    public ClienteService(ClienteRepository clienteRepository, EventoRepository eventoRepository) {
        this.clienteRepository = clienteRepository;
        this.eventoRepository = eventoRepository;
    }

    @Transactional
    public Cliente buscarOuCriar(String cpf, Integer eventoId) {
        return clienteRepository.findByCpf(cpf)
            .orElseGet(() -> {
                Evento evento = eventoRepository.findById(eventoId)
                    .orElseThrow(() -> new ResourceNotFoundException("Evento ID " + eventoId + " inválido."));

                Cliente novoCliente = new Cliente();
                novoCliente.setCpf(cpf);
                novoCliente.setSaldo(evento.getCreditoInicialCliente());
                
                return clienteRepository.save(novoCliente);
            });
    }

    @Transactional
    public Cliente criarCliente(String cpf, Integer eventoId) {
        
        Evento evento = eventoRepository.findById(eventoId)
            .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado para definir crédito inicial."));

        Cliente cliente = new Cliente();
        cliente.setCpf(cpf);
        
        cliente.setSaldo(evento.getCreditoInicialCliente()); 

        return clienteRepository.save(cliente);
    }


    public boolean verificarCliente(String cpf) {
        return clienteRepository.existsByCpf(cpf);
    }

    public Cliente buscarPorCpf(String cpf) {
        return clienteRepository.findByCpf(cpf)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado."));
    }
}