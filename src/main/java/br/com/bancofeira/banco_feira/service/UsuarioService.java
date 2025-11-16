package br.com.bancofeira.banco_feira.service;

import br.com.bancofeira.banco_feira.model.ConfirmationToken;
import br.com.bancofeira.banco_feira.model.Role;
import br.com.bancofeira.banco_feira.model.Usuario;
import br.com.bancofeira.banco_feira.repository.ConfirmationTokenRepository;
import br.com.bancofeira.banco_feira.repository.RoleRepository;
import br.com.bancofeira.banco_feira.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Service
public class UsuarioService {
    private final RoleRepository roleRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final ConfirmationTokenRepository tokenRepository;


    public UsuarioService(UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder,
                          RoleRepository roleRepository,
                          EmailService emailService, ConfirmationTokenRepository tokenRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.emailService = emailService;
        this.tokenRepository = tokenRepository;
    }

    @Transactional
    public Usuario criarUsuario(Usuario usuario) {
        if (usuarioRepository.findByCpf(usuario.getCpf()).isPresent()) {
            throw new IllegalStateException("CPF já cadastrado no sistema.");
        }
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new IllegalStateException("E-mail já cadastrado no sistema.");
        }

        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        Role clientRole = roleRepository.findByNome("ROLE_CLIENTE")
                .orElseThrow(() -> new RuntimeException("Configuração crítica: ROLE_CLIENTE não encontrado."));
        usuario.setRoles(Set.of(clientRole));

        Usuario novoUsuario = usuarioRepository.save(usuario);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                novoUsuario
        );
        tokenRepository.save(confirmationToken);

        try {
            emailService.enviarEmailDeConfirmacao(novoUsuario, token);
        } catch (Exception e) {
            System.err.println("Falha ao enviar e-mail de confirmação: " + e.getMessage());
        }

        return novoUsuario;
    }
    public java.util.List<Usuario> listarTodos(){
        return usuarioRepository.findAll();

    }

    @SuppressWarnings("null")
    public Usuario buscarPorId(Integer id){
        return usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));
    }


    
}
