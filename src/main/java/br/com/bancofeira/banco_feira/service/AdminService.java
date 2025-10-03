package br.com.bancofeira.banco_feira.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.bancofeira.banco_feira.exception.ResourceNotFoundException;
import br.com.bancofeira.banco_feira.model.Role;
import br.com.bancofeira.banco_feira.model.SolicitacaoAcessoEmpresa;
import br.com.bancofeira.banco_feira.model.StatusSolicitacao;
import br.com.bancofeira.banco_feira.model.Usuario;
import br.com.bancofeira.banco_feira.repository.RoleRepository;
import br.com.bancofeira.banco_feira.repository.SolicitacaoAcessoEmpresaRepository;
import br.com.bancofeira.banco_feira.repository.UsuarioRepository;

@Service
public class AdminService {

    private final SolicitacaoAcessoEmpresaRepository solicitacaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;

    public AdminService(SolicitacaoAcessoEmpresaRepository solicitacaoRepository, UsuarioRepository usuarioRepository, RoleRepository roleRepository) {
        this.solicitacaoRepository = solicitacaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
    }

    public List<SolicitacaoAcessoEmpresa> listarSolicitacoesPendentes() {
        return solicitacaoRepository.findByStatus(StatusSolicitacao.PENDENTE);
    }

    
    @Transactional
    public SolicitacaoAcessoEmpresa aprovarSolicitacao(Integer solicitacaoId) {
      
        SolicitacaoAcessoEmpresa solicitacao = solicitacaoRepository.findById(solicitacaoId)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitação não encontrada com o ID: " + solicitacaoId));

     
        if (solicitacao.getStatus() != StatusSolicitacao.PENDENTE) {
            throw new IllegalStateException("Esta solicitação não está mais pendente e não pode ser aprovada.");
        }

   
        Usuario usuario = solicitacao.getUsuario();
        
    
        Role roleEmpresa = roleRepository.findByNome("ROLE_EMPRESA")
                .orElseThrow(() -> new RuntimeException("Configuração do sistema incompleta: Role 'ROLE_EMPRESA' não encontrada."));

    
        usuario.getRoles().add(roleEmpresa);
        usuarioRepository.save(usuario);

  
        solicitacao.setStatus(StatusSolicitacao.APROVADO);
        return solicitacaoRepository.save(solicitacao);
    }

    @Transactional
    public SolicitacaoAcessoEmpresa rejeitarSolicitacao(Integer solicitacaoId) {
 
        SolicitacaoAcessoEmpresa solicitacao = solicitacaoRepository.findById(solicitacaoId)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitação não encontrada com o ID: " + solicitacaoId));

 
        if (solicitacao.getStatus() != StatusSolicitacao.PENDENTE) {
            throw new IllegalStateException("Esta solicitação não está mais pendente e não pode ser rejeitada.");
        }

        solicitacao.setStatus(StatusSolicitacao.REJEITADO);
        return solicitacaoRepository.save(solicitacao);
    }
}