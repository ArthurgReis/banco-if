package br.com.bancofeira.banco_feira.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.bancofeira.banco_feira.model.Empresa;
import br.com.bancofeira.banco_feira.model.Evento;
import br.com.bancofeira.banco_feira.model.Usuario;
import br.com.bancofeira.banco_feira.repository.EmpresaRepository;
import br.com.bancofeira.banco_feira.repository.EventoRepository;
import br.com.bancofeira.banco_feira.repository.RoleRepository;
import br.com.bancofeira.banco_feira.repository.UsuarioRepository;
import jakarta.transaction.Transactional;

@Service
public class AdminService {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final EventoRepository eventoRepository;
    private final EmpresaRepository empresaRepository; 

    

    public AdminService(UsuarioRepository usuarioRepository, RoleRepository roleRepository,
            EventoRepository eventoRepository, EmpresaRepository empresaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
        this.eventoRepository = eventoRepository;
        this.empresaRepository = empresaRepository;
    }

    @Transactional
    public Evento criarEvento(Evento evento) {
        
        String chave = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        evento.setChaveInscricao(chave);
        return eventoRepository.save(evento);
    }

    public List<Usuario> listarTodosUsuarios() {
        return usuarioRepository.findAll();
    }

    public List<Empresa> listarTodasEmpresas() {
        return empresaRepository.findAll();
    }

 
 
}