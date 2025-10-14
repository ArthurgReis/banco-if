package br.com.bancofeira.banco_feira.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.bancofeira.banco_feira.dto.InscricaoEmpresaDto;
import br.com.bancofeira.banco_feira.exception.ResourceNotFoundException;
import br.com.bancofeira.banco_feira.model.Empresa;
import br.com.bancofeira.banco_feira.model.Evento;
import br.com.bancofeira.banco_feira.model.Inscricao;
import br.com.bancofeira.banco_feira.model.Role;
import br.com.bancofeira.banco_feira.model.StatusEmpresa;
import br.com.bancofeira.banco_feira.model.Usuario;
import br.com.bancofeira.banco_feira.repository.EmpresaRepository;
import br.com.bancofeira.banco_feira.repository.EventoRepository;
import br.com.bancofeira.banco_feira.repository.InscricaoRepository;
import br.com.bancofeira.banco_feira.repository.RoleRepository;
import br.com.bancofeira.banco_feira.repository.UsuarioRepository;

@Service
public class InscricaoService {


    private final InscricaoRepository inscricaoRepository;
    private final EventoRepository eventoRepository;
    private final RoleRepository roleRepository;
    private final EmpresaRepository empresaRepository;  
    private final UsuarioRepository usuarioRepository;

   
    

    public InscricaoService(InscricaoRepository inscricaoRepository, EventoRepository eventoRepository,
            RoleRepository roleRepository, EmpresaRepository empresaRepository, UsuarioRepository usuarioRepository) {
        this.inscricaoRepository = inscricaoRepository;
        this.eventoRepository = eventoRepository;
        this.roleRepository = roleRepository;
        this.empresaRepository = empresaRepository;
        this.usuarioRepository = usuarioRepository;
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

    @Transactional
    public Empresa inscreverEmpresaEmEvento(InscricaoEmpresaDto inscricaoDto, Usuario dono) {
        Evento evento = eventoRepository.findByChaveInscricao(inscricaoDto.getChaveInscricao())
                .orElseThrow(() -> new ResourceNotFoundException("Chave de inscrição inválida ou evento não encontrado!"));

        Role roleEmpresa = roleRepository.findByNome("ROLE_EMPRESA").orElseThrow();
        Role roleCliente = roleRepository.findByNome("ROLE_CLIENTE").orElseThrow();
        dono.getRoles().add(roleEmpresa);
        dono.getRoles().add(roleCliente);

        Empresa novaEmpresa = new Empresa();
        novaEmpresa.setNomeFantasia(inscricaoDto.getNomeFantasia());
        novaEmpresa.setDescricaoCurta(inscricaoDto.getDescricaoCurta());
        novaEmpresa.setStatus(StatusEmpresa.APROVADO);
        novaEmpresa.setEvento(evento);

        empresaRepository.save(novaEmpresa); 

        dono.getEmpresas().add(novaEmpresa);
        usuarioRepository.save(dono);

        return novaEmpresa;
    }
}