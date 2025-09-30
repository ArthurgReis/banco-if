package br.com.bancofeira.banco_feira.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.bancofeira.banco_feira.model.SolicitacaoAcessoEmpresa;
import br.com.bancofeira.banco_feira.model.StatusSolicitacao;
import br.com.bancofeira.banco_feira.repository.RoleRepository;
import br.com.bancofeira.banco_feira.repository.SolicitacaoAcessoEmpresaRepository;
import br.com.bancofeira.banco_feira.repository.UsuarioRepository;

@Service
public class AdminService {

    private final SolicitacaoAcessoEmpresaRepository solicitacaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;

    public AdminService(SolicitacaoAcessoEmpresaRepository solicitacaoRepository, UsuarioRepository usuarioRepository,
            RoleRepository roleRepository) {
        this.solicitacaoRepository = solicitacaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
    }

    public List<SolicitacaoAcessoEmpresa> listarSolicitacoesPendentes(){
        return solicitacaoRepository.findByStatus(StatusSolicitacao.PENDENTE);
    }
    

    

    
}
