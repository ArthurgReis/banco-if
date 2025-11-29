package br.com.bancofeira.banco_feira.service;

import br.com.bancofeira.banco_feira.model.Role;
import br.com.bancofeira.banco_feira.model.Usuario;
import br.com.bancofeira.banco_feira.repository.RoleRepository;
import br.com.bancofeira.banco_feira.repository.UsuarioRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class UsuarioService {
    private final RoleRepository roleRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;


    public UsuarioService(UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder,
                          RoleRepository roleRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public Usuario criarUsuario(Usuario usuario) {
        if (usuarioRepository.findByCpf(usuario.getCpf()).isPresent()) {
            throw new IllegalStateException("CPF já cadastrado no sistema.");
        }
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new IllegalStateException("E-mail já cadastrado no sistema.");
        }

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        Role clientRole = roleRepository.findByNome("ROLE_CLIENTE")
                .orElseThrow(() -> new RuntimeException("Configuração crítica: ROLE_CLIENTE não encontrado."));
        usuario.setRoles(Set.of(clientRole));

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
    public void alterarSenha(Usuario usuario, String senhaAtual, String senhaNova){
        if (!passwordEncoder.matches(senhaAtual, usuario.getSenha())) {
            throw new BadCredentialsException("A senha atual informada está incorreta.");
        }

        usuario.setSenha(passwordEncoder.encode(senhaNova));
        usuarioRepository.save(usuario);
    }
}