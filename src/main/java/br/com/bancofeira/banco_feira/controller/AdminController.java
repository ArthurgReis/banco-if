package br.com.bancofeira.banco_feira.controller;

import br.com.bancofeira.banco_feira.dto.EmpresaResponseDto;
import br.com.bancofeira.banco_feira.dto.UsuarioResponseDto;
import br.com.bancofeira.banco_feira.exception.ResourceNotFoundException;
import br.com.bancofeira.banco_feira.model.ApiResponse;
import br.com.bancofeira.banco_feira.model.Empresa;
import br.com.bancofeira.banco_feira.model.Inscricao;
import br.com.bancofeira.banco_feira.model.Usuario;
import br.com.bancofeira.banco_feira.repository.EmpresaRepository;
import br.com.bancofeira.banco_feira.repository.EventoRepository;
import br.com.bancofeira.banco_feira.repository.InscricaoRepository;
import br.com.bancofeira.banco_feira.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin") 
public class AdminController {

    private final AdminService adminService;
    private final EventoRepository eventoRepository;
    private final InscricaoRepository inscricaoRepository;
    private final EmpresaRepository empresaRepository;

    public AdminController(AdminService adminService, EventoRepository eventoRepository, InscricaoRepository inscricaoRepository, EmpresaRepository empresaRepository) {
        this.adminService = adminService;
        this.eventoRepository = eventoRepository;
        this.inscricaoRepository = inscricaoRepository;
        this.empresaRepository = empresaRepository;
    }

    @GetMapping("/usuarios")
    public ResponseEntity<ApiResponse<List<UsuarioResponseDto>>> listarUsuarios() {
        List<Usuario> usuarios = adminService.listarTodosUsuarios();
        
        List<UsuarioResponseDto> dtos = usuarios.stream()
                .map(UsuarioResponseDto::fromEntity)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(new ApiResponse<>(true, "Usuários listados com sucesso.", dtos));
    }

    @GetMapping("/empresas")
    public ResponseEntity<ApiResponse<List<EmpresaResponseDto>>> listarEmpresas() {
        List<Empresa> empresas = adminService.listarTodasEmpresas();

        List<EmpresaResponseDto> dtos = empresas.stream()
                .map(EmpresaResponseDto::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse<>(true, "Empresas listadas com sucesso.", dtos));
    }
    public List<Inscricao> listarClientesPorEvento(Integer eventoId) {
        if (!eventoRepository.existsById(eventoId)) {
            throw new ResourceNotFoundException("Evento não encontrado com o ID: " + eventoId);
        }
        return inscricaoRepository.findByEventoId(eventoId);
    }

    // --- NOVO MÉTODO (Requisito do Admin) ---
    public List<Empresa> listarEmpresasPorEvento(Integer eventoId) {
        if (!eventoRepository.existsById(eventoId)) {
            throw new ResourceNotFoundException("Evento não encontrado com o ID: " + eventoId);
        }
        return empresaRepository.findByEventoId(eventoId);
    }

}