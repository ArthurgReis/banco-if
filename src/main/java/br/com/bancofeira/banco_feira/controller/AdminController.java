package br.com.bancofeira.banco_feira.controller;

import br.com.bancofeira.banco_feira.dto.AdminEmpresaDetalheDto;
import br.com.bancofeira.banco_feira.dto.EmpresaRankingDto;
import br.com.bancofeira.banco_feira.dto.EmpresaResponseDto;
import br.com.bancofeira.banco_feira.dto.EventoRequestDto;
import br.com.bancofeira.banco_feira.dto.EventoResponseDto;
import br.com.bancofeira.banco_feira.dto.UsuarioResponseDto;
import br.com.bancofeira.banco_feira.exception.ResourceNotFoundException;
import br.com.bancofeira.banco_feira.model.*;
import br.com.bancofeira.banco_feira.repository.EmpresaRepository;
import br.com.bancofeira.banco_feira.repository.EventoRepository;
import br.com.bancofeira.banco_feira.service.AdminService;
import br.com.bancofeira.banco_feira.service.EventoService;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin") 
public class AdminController {

    private final AdminService adminService;
    private final EventoRepository eventoRepository;
    private final EmpresaRepository empresaRepository;
    private final EventoService eventoService;

    public AdminController(AdminService adminService, EventoRepository eventoRepository, EmpresaRepository empresaRepository, EventoService eventoService) {
        this.adminService = adminService;
        this.eventoRepository = eventoRepository;
        this.empresaRepository = empresaRepository;
        this.eventoService = eventoService;
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


    @SuppressWarnings("null")
    @GetMapping("/evento/{eventoId}/empresas")
    public ResponseEntity<ApiResponse<List<EmpresaResponseDto>>> listarEmpresasPorEvento(@PathVariable Integer eventoId) {
        if (!eventoRepository.existsById(eventoId)) {
            throw new ResourceNotFoundException("Evento não encontrado com o ID: " + eventoId);
        }
        List<Empresa> empresas = empresaRepository.findByEventoId(eventoId);

        List<EmpresaResponseDto> empresasDto = empresas.stream().map(EmpresaResponseDto::fromEntity).collect(Collectors.toList());
        return ResponseEntity.ok( new ApiResponse<>(true, "Empresas Listadas por evento com sucesso", empresasDto));
    }

    @GetMapping("/eventos")
    public ResponseEntity<ApiResponse<List<EventoResponseDto>>> buscarEventosAdmin() {
        List<Evento> eventos = eventoService.listarEventos();

        List<EventoResponseDto> eventosDto = eventos.stream().map(EventoResponseDto::fromEntity).collect(Collectors.toList());

        return ResponseEntity.ok( new ApiResponse<>(true, "Eventos listados com sucesso", eventosDto));

    }

    @GetMapping("/eventos/{eventoId}/ranking")
    public ResponseEntity<ApiResponse<List<EmpresaRankingDto>>> getRankingEmpresasPorEvento(@PathVariable Integer eventoId) {
        
        List<EmpresaRankingDto> ranking = adminService.listarRankingEmpresas(eventoId);
        
        ApiResponse<List<EmpresaRankingDto>> response = new ApiResponse<>(true, "Ranking de empresas gerado.", ranking);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/empresas/{id}")
    public ResponseEntity<ApiResponse<AdminEmpresaDetalheDto>> getDetalhesDaEmpresa(@PathVariable Integer id) {
        
        Empresa empresa = adminService.buscarEmpresaDetalhes(id);
        
        AdminEmpresaDetalheDto dto = AdminEmpresaDetalheDto.fromEntity(empresa);
        
        ApiResponse<AdminEmpresaDetalheDto> response = new ApiResponse<>(true, "Detalhes da empresa obtidos.", dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EventoResponseDto>> atualizarEvento(
            @PathVariable Integer id,
            @RequestBody @Valid EventoRequestDto dto) {

        Evento eventoDados = new Evento();
        eventoDados.setNome(dto.getNome());
        eventoDados.setDataRealizacao(dto.getDataRealizacao());
        eventoDados.setCreditoInicialCliente(dto.getCreditoInicialCliente());

        Evento eventoAtualizado = eventoService.atualizarEvento(id, eventoDados);

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Evento atualizado com sucesso!",
                EventoResponseDto.fromEntity(eventoAtualizado)
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletarEvento(@PathVariable Integer id) {
        
        eventoService.deletarEvento(id);

        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Evento deletado com sucesso!",
                null
        ));
    }

}