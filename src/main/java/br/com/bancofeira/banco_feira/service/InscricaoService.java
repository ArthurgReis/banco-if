package br.com.bancofeira.banco_feira.service;

import br.com.bancofeira.banco_feira.exception.ResourceNotFoundException;
import br.com.bancofeira.banco_feira.model.Evento;
import br.com.bancofeira.banco_feira.model.Inscricao;
import br.com.bancofeira.banco_feira.model.Role;
import br.com.bancofeira.banco_feira.model.Usuario;
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
    // Removemos os repositórios de Empresa e Usuário

    public InscricaoService(InscricaoRepository inscricaoRepository, EventoRepository eventoRepository, RoleRepository roleRepository) {
        this.inscricaoRepository = inscricaoRepository;
        this.eventoRepository = eventoRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public Inscricao inscreverClienteEmEvento(String chaveCliente, Usuario usuario) {
        Evento evento = eventoRepository.findByChaveCliente(chaveCliente)
                .orElseThrow(() -> new ResourceNotFoundException("Chave de inscrição de cliente inválida!"));

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

    // O método 'inscreverEmpresaEmEvento' FOI REMOVIDO DAQUI.
}