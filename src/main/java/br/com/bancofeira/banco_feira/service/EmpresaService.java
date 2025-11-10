package br.com.bancofeira.banco_feira.service;

import br.com.bancofeira.banco_feira.dto.EmpresaCreateDto;
import br.com.bancofeira.banco_feira.exception.ResourceNotFoundException;
import br.com.bancofeira.banco_feira.model.*;
import br.com.bancofeira.banco_feira.repository.EmpresaRepository;
import br.com.bancofeira.banco_feira.repository.EventoRepository;
import br.com.bancofeira.banco_feira.repository.RoleRepository;
import br.com.bancofeira.banco_feira.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final EventoRepository eventoRepository;
    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;

    public EmpresaService(EmpresaRepository empresaRepository,
                          EventoRepository eventoRepository,
                          UsuarioRepository usuarioRepository,
                          RoleRepository roleRepository) {
        this.empresaRepository = empresaRepository;
        this.eventoRepository = eventoRepository;
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public Empresa criarEmpresa(EmpresaCreateDto dto, Usuario dono) {
        Evento evento = eventoRepository.findByChaveEmpresa(dto.getChaveEmpresa())
                .orElseThrow(() -> new ResourceNotFoundException("Chave de inscrição de empresa inválida!"));

        Role roleEmpresa = roleRepository.findByNome("ROLE_EMPRESA")
                .orElseThrow(() -> new RuntimeException("Configuração crítica: ROLE_EMPRESA não encontrado."));
        Role roleCliente = roleRepository.findByNome("ROLE_CLIENTE")
                .orElseThrow(() -> new RuntimeException("Configuração crítica: ROLE_CLIENTE não encontrado."));
        dono.getRoles().add(roleEmpresa);
        dono.getRoles().add(roleCliente);

        Empresa novaEmpresa = new Empresa();
        novaEmpresa.setNomeFantasia(dto.getNomeFantasia());
        novaEmpresa.setDescricaoCurta(dto.getDescricaoCurta());
        novaEmpresa.setStatus(StatusEmpresa.APROVADO);
        novaEmpresa.setEvento(evento);

        empresaRepository.save(novaEmpresa);

        dono.getEmpresas().add(novaEmpresa);
        usuarioRepository.save(dono);

        return novaEmpresa;
    }

}