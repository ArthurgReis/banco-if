package br.com.bancofeira.banco_feira.service;

import br.com.bancofeira.banco_feira.exception.ResourceNotFoundException;
import br.com.bancofeira.banco_feira.model.*;
import br.com.bancofeira.banco_feira.repository.EventoRepository;
import br.com.bancofeira.banco_feira.repository.InscricaoRepository;
import br.com.bancofeira.banco_feira.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InscricaoService {

    private final InscricaoRepository inscricaoRepository;
    private final EventoRepository eventoRepository;
    private final RoleRepository roleRepository;

    public InscricaoService(InscricaoRepository inscricaoRepository, EventoRepository eventoRepository, RoleRepository roleRepository) {
        this.inscricaoRepository = inscricaoRepository;
        this.eventoRepository = eventoRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public Inscricao inscreverClienteEmEvento(String chaveInscricao, Usuario usuario) {
        Evento evento = eventoRepository.findByChaveInscricao(chaveInscricao)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado com esta chave de inscrição."));

        if (inscricaoRepository.existsByUsuarioAndEvento(usuario, evento)) {
            throw new IllegalStateException("Usuário já inscrito neste evento.");
        }

        Role clientRole = roleRepository.findByNome("ROLE_CLIENTE")
                .orElseThrow(() -> new RuntimeException("Role 'ROLE_CLIENTE' não encontrada."));
        usuario.getRoles().add(clientRole); 

        Inscricao novaInscricao = new Inscricao();
        novaInscricao.setUsuario(usuario);
        novaInscricao.setEvento(evento);

   
        novaInscricao.setCreditos(evento.getCreditoInicialCliente());

        return inscricaoRepository.save(novaInscricao);
    }
}