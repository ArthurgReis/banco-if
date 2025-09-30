package br.com.bancofeira.banco_feira.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.bancofeira.banco_feira.model.SolicitacaoAcessoEmpresa;
import br.com.bancofeira.banco_feira.model.StatusSolicitacao;
import br.com.bancofeira.banco_feira.model.Usuario;
import br.com.bancofeira.banco_feira.repository.SolicitacaoAcessoEmpresaRepository;
import br.com.bancofeira.banco_feira.repository.UsuarioRepository;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final SolicitacaoAcessoEmpresaRepository solicitacaoRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, SolicitacaoAcessoEmpresaRepository solicitacaoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.solicitacaoRepository = solicitacaoRepository;
    }

    public Usuario criarUsuario(Usuario usuario){
        if(usuarioRepository.findByCpf(usuario.getCpf()).isPresent()){
            throw new IllegalStateException("CPF já cadastrado no sistema.");
        }
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);
        
        return usuarioRepository.save(usuario);
    }

    public java.util.List<Usuario> listarTodos(){
        return usuarioRepository.findAll();

    }

    public Usuario buscarPorId(Integer id){
        return usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));
    }

    @Transactional
    public SolicitacaoAcessoEmpresa solicitarAcessoEmpresa(Integer usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

        boolean jaPossuiRoleEmpresa = usuario.getRoles().stream()
                .anyMatch(role -> role.getNome().equals("ROLE_EMPRESA"));
        if (jaPossuiRoleEmpresa) {
            throw new IllegalStateException("Usuário já possui permissão de empresa.");
        }

        solicitacaoRepository.findByUsuarioIdAndStatus(usuarioId, StatusSolicitacao.PENDENTE)
                .ifPresent(s -> {
                    throw new IllegalStateException("Usuário já possui uma solicitação pendente.");
                });

        SolicitacaoAcessoEmpresa novaSolicitacao = new SolicitacaoAcessoEmpresa();
        novaSolicitacao.setUsuario(usuario);
        novaSolicitacao.setStatus(StatusSolicitacao.PENDENTE);

        return solicitacaoRepository.save(novaSolicitacao);
    }
    
}
