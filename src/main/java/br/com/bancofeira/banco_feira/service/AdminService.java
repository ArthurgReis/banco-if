package br.com.bancofeira.banco_feira.service;

import java.util.List;
import org.springframework.stereotype.Service;

import br.com.bancofeira.banco_feira.model.Empresa;
import br.com.bancofeira.banco_feira.model.Usuario;
import br.com.bancofeira.banco_feira.repository.EmpresaRepository;
import br.com.bancofeira.banco_feira.repository.EventoRepository;
import br.com.bancofeira.banco_feira.repository.RoleRepository;
import br.com.bancofeira.banco_feira.repository.UsuarioRepository;

@Service
public class AdminService {

    private final UsuarioRepository usuarioRepository;
    @SuppressWarnings("unused")
    private final RoleRepository roleRepository;
    @SuppressWarnings("unused")
    private final EventoRepository eventoRepository;
    private final EmpresaRepository empresaRepository; 

    

    public AdminService(UsuarioRepository usuarioRepository, RoleRepository roleRepository,
            EventoRepository eventoRepository, EmpresaRepository empresaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
        this.eventoRepository = eventoRepository;
        this.empresaRepository = empresaRepository;
    }

    public List<Usuario> listarTodosUsuarios() {
        return usuarioRepository.findAll();
    }

    public List<Empresa> listarTodasEmpresas() {
        return empresaRepository.findAll();
    }

 
 
}