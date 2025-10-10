package br.com.bancofeira.banco_feira.service;

import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.bancofeira.banco_feira.model.Role;
import br.com.bancofeira.banco_feira.model.Usuario;
import br.com.bancofeira.banco_feira.repository.RoleRepository;
import br.com.bancofeira.banco_feira.repository.UsuarioRepository;

@Service
public class UsuarioService {
    private final RoleRepository roleRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public Usuario criarUsuario(Usuario usuario){
        if(usuarioRepository.findByCpf(usuario.getCpf()).isPresent()){
            throw new IllegalStateException("CPF já cadastrado no sistema.");
        }
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);

        Role clientRole = roleRepository.findByNome("ROLE_CLIENTE")
                .orElseThrow(() -> new RuntimeException("Role 'ROLE_CLIENTE' não encontrada."));
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


    
}
