package br.com.bancofeira.banco_feira.service;

import br.com.bancofeira.banco_feira.dto.EmpresaCreateDto;
import br.com.bancofeira.banco_feira.dto.JuntarEmpresaDto;
import br.com.bancofeira.banco_feira.exception.ResourceNotFoundException;
import br.com.bancofeira.banco_feira.model.*;
import br.com.bancofeira.banco_feira.repository.EmpresaRepository;
import br.com.bancofeira.banco_feira.repository.EventoRepository;
import br.com.bancofeira.banco_feira.repository.RoleRepository;
import br.com.bancofeira.banco_feira.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.UUID;

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

        String chaveFunc = UUID.randomUUID().toString().substring(18, 26).toUpperCase();
        novaEmpresa.setChaveFuncionario(chaveFunc);

        empresaRepository.save(novaEmpresa);

        dono.getEmpresas().add(novaEmpresa);
        usuarioRepository.save(dono);

        return novaEmpresa;
    }

    public List<Empresa> listarMinhasEmpresasPorEvento(Integer eventoId, Usuario usuarioLogado) {

        if (!eventoRepository.existsById(eventoId)) {
            throw new ResourceNotFoundException("Evento não encontrado com o ID: " + eventoId);
        }

        return empresaRepository.findByEventoIdAndFuncionariosContains(eventoId, usuarioLogado);
    }


    public Empresa getMinhaEmpresa(Integer empresaId, Usuario usuarioLogado) {
        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada."));

        boolean temPermissao = usuarioLogado.getEmpresas().stream()
                .anyMatch(emp -> emp.getId().equals(empresa.getId()));

        if (!temPermissao) {
            throw new IllegalStateException("Acesso negado. Você não gerencia esta empresa.");
        }

        return empresa;
    }

    @Transactional
    public Empresa adicionarFuncionario(JuntarEmpresaDto dto, Usuario novoFuncionario) {
        Empresa empresa = empresaRepository.findByChaveFuncionario(dto.getChaveFuncionario())
                .orElseThrow(() -> new ResourceNotFoundException("Chave de funcionário inválida!"));

        if (novoFuncionario.getEmpresas().stream().anyMatch(e -> e.getId().equals(empresa.getId()))) {
            throw new IllegalStateException("Você já faz parte desta empresa.");
        }

        Role roleEmpresa = roleRepository.findByNome("ROLE_EMPRESA")
            .orElseThrow(() -> new RuntimeException("Configuração crítica: ROLE_EMPRESA não encontrado."));
        Role roleCliente = roleRepository.findByNome("ROLE_CLIENTE")
            .orElseThrow(() -> new RuntimeException("Configuração crítica: ROLE_CLIENTE não encontrado."));
        novoFuncionario.getRoles().add(roleEmpresa);
        novoFuncionario.getRoles().add(roleCliente);

        novoFuncionario.getEmpresas().add(empresa);
        usuarioRepository.save(novoFuncionario);

        return empresa;
    }



}