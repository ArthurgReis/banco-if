package br.com.bancofeira.banco_feira.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.bancofeira.banco_feira.dto.InscricaoEmpresaDto;
import br.com.bancofeira.banco_feira.exception.ResourceNotFoundException;
import br.com.bancofeira.banco_feira.model.Empresa;
import br.com.bancofeira.banco_feira.model.Evento;
import br.com.bancofeira.banco_feira.model.Role;
import br.com.bancofeira.banco_feira.model.StatusEmpresa;
import br.com.bancofeira.banco_feira.model.Usuario;
import br.com.bancofeira.banco_feira.repository.EmpresaRepository;
import br.com.bancofeira.banco_feira.repository.EventoRepository;
import br.com.bancofeira.banco_feira.repository.RoleRepository;
import br.com.bancofeira.banco_feira.repository.UsuarioRepository;

@Service
public class EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final EventoRepository eventoRepository;

    public EmpresaService(EmpresaRepository empresaRepository, UsuarioRepository usuarioRepository, RoleRepository roleRepository, EventoRepository eventoRepository) {
        this.empresaRepository = empresaRepository;
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
        this.eventoRepository = eventoRepository;
    }

    @Transactional
    public Empresa inscreverEmpresa(InscricaoEmpresaDto inscricaoDto, Usuario dono) {
        Evento evento = eventoRepository.findByChaveInscricao(inscricaoDto.getChaveInscricao())
                .orElseThrow(() -> new ResourceNotFoundException("Chave de inscrição inválida ou evento não encontrado!"));

        Role roleEmpresa = roleRepository.findByNome("ROLE_EMPRESA")
                .orElseThrow(() -> new RuntimeException("Role 'ROLE_EMPRESA' não encontrada."));
        dono.getRoles().add(roleEmpresa);

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