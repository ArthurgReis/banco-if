package br.com.bancofeira.banco_feira.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.bancofeira.banco_feira.model.Empresa;
import br.com.bancofeira.banco_feira.model.Usuario;
import br.com.bancofeira.banco_feira.repository.EmpresaRepository;
import br.com.bancofeira.banco_feira.repository.RoleRepository;
import br.com.bancofeira.banco_feira.repository.UsuarioRepository;

@Service
public class EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;

    public EmpresaService(EmpresaRepository empresaRepository, UsuarioRepository usuarioRepository, RoleRepository roleRepository) {
        this.empresaRepository = empresaRepository;
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public Empresa criarEmpresa(Empresa empresa, Integer usuarioDonoId) {
    
        Usuario dono = usuarioRepository.findById(usuarioDonoId)
                .orElseThrow(() -> new RuntimeException("Usuário (dono) não encontrado!"));

        boolean temPermissao = dono.getRoles().stream()
            .anyMatch(role -> role.getNome().equals("ROLE_EMPRESA"));

        if(!temPermissao){
            throw new IllegalStateException("Este usuário não tem permissão para criar uma empresa");
        }
 
        Empresa novaEmpresa = empresaRepository.save(empresa);

        dono.getEmpresas().add(novaEmpresa);
        usuarioRepository.save(dono);

        return novaEmpresa;
    }
}